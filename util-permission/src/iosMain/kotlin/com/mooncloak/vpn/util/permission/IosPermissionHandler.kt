package com.mooncloak.vpn.util.permission

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.coroutines.resume

public operator fun PermissionHandler.Companion.invoke(): PermissionHandler =
    IosPermissionHandler()

internal class IosPermissionHandler internal constructor() : PermissionHandler {

    override suspend fun hasPermissions(vararg permissions: Permission): Boolean =
        permissions.all { permission ->
            when (permission) {
                Permission.PostNotification -> suspendCancellableCoroutine { continuation ->
                    UNUserNotificationCenter.currentNotificationCenter()
                        .getNotificationSettingsWithCompletionHandler { settings ->
                            continuation.resume(settings?.authorizationStatus == UNAuthorizationStatusAuthorized)
                        }
                }

                else -> false
            }
        }

    override suspend fun shouldShowRequestPermissionRationale(vararg permissions: Permission): Boolean = false

    override suspend fun requestPermission(vararg permissions: Permission): PermissionResult {
        val permissionGrantStatuses = permissions.associateWith { permission ->
            when (permission) {
                Permission.PostNotification -> requestNotificationPermission()
                else -> false
            }
        }

        return PermissionResult(
            permissions = permissionGrantStatuses
        )
    }

    private suspend fun requestNotificationPermission(): Boolean =
        suspendCancellableCoroutine { continuation ->
            UNUserNotificationCenter.currentNotificationCenter()
                .requestAuthorizationWithOptions(
                    UNAuthorizationOptionAlert + UNAuthorizationOptionBadge + UNAuthorizationOptionSound
                ) { granted, _ ->
                    continuation.resume(granted)
                }
        }
}
