package com.mooncloak.vpn.util.permission

public interface PermissionHandler {

    public suspend fun hasPermissions(vararg permissions: Permission): Boolean

    public suspend fun shouldShowRequestPermissionRationale(vararg permissions: Permission): Boolean

    public suspend fun requestPermission(vararg permissions: Permission): PermissionResult

    public companion object
}
