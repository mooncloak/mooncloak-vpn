package com.mooncloak.vpn.util.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

public operator fun PermissionHandler.Companion.invoke(activity: ComponentActivity): PermissionHandler =
    AndroidPermissionHandler(activity = activity)

internal class AndroidPermissionHandler internal constructor(
    private val activity: ComponentActivity
) : PermissionHandler {

    override suspend fun hasPermissions(vararg permissions: Permission): Boolean =
        permissions.all { permission ->
            val value = permission.value

            when {
                value.isBlank() -> true
                value == Manifest.permission.POST_NOTIFICATIONS && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> true
                else -> ContextCompat.checkSelfPermission(activity, value) == PackageManager.PERMISSION_GRANTED
            }
        }

    override suspend fun shouldShowRequestPermissionRationale(vararg permissions: Permission): Boolean =
        permissions.any { permission ->
            val value = permission.value

            when {
                value.isBlank() -> false
                else -> activity.shouldShowRequestPermissionRationale(value)
            }
        }

    override suspend fun requestPermission(vararg permissions: Permission): PermissionResult {
        // Filter out POST_NOTIFICATIONS on pre-Android 13
        val filteredPermissions = permissions
            .map { it.value }
            .filter { value -> value.isNotBlank() }
            .toTypedArray()

        if (filteredPermissions.isEmpty()) return PermissionResult()

        return suspendCancellableCoroutine { continuation ->
            val launcher = activity.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissionResultMap ->
                val result = PermissionResult(
                    permissions = permissionResultMap.mapKeys { entry -> Permission(value = entry.key) }
                )

                continuation.resume(result)
            }

            launcher.launch(filteredPermissions)

            continuation.invokeOnCancellation { launcher.unregister() }
        }
    }
}
