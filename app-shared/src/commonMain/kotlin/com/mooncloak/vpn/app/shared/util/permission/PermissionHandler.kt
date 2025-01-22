package com.mooncloak.vpn.app.shared.util.permission

public interface PermissionHandler {

    public suspend fun hasPermissions(vararg permissions: Permission): Boolean

    public suspend fun shouldShowRequestPermissionRationale(vararg permissions: Permission)

    public suspend fun requestPermission(vararg permissions: Permission)

    public companion object
}
