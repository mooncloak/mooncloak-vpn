package com.mooncloak.vpn.util.permission

public operator fun PermissionHandler.Companion.invoke(): PermissionHandler =
    JvmPermissionHandler()

// JVM/Desktop doesn't have the concept of OS permissions. So we always default to all permissions are granted.
internal class JvmPermissionHandler internal constructor() : PermissionHandler {

    override suspend fun hasPermissions(vararg permissions: Permission): Boolean = true

    override suspend fun shouldShowRequestPermissionRationale(vararg permissions: Permission): Boolean = false

    override suspend fun requestPermission(vararg permissions: Permission): PermissionResult =
        PermissionResult(permissions = permissions.associateWith { true })
}
