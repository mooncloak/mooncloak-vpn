package com.mooncloak.vpn.util.permission

/**
 * Represents the result of requesting permissions for the user.
 *
 * @param [permissions] A [Map] of [Permission] keys and [Boolean] values. A value of `true` indicates the permission
 * was granted, where a value of `false` indicates the permission was denied.
 */
public data class PermissionResult public constructor(
    public val permissions: Map<Permission, Boolean> = emptyMap()
)

/**
 * Retrieves the [Set] of the granted [Permission]s.
 */
public val PermissionResult.grantedPermissions: Set<Permission>
    inline get() = this.permissions.filter { entry -> entry.value }.keys

/**
 * Retrieves the [Set] of the denied [Permission]s.
 */
public val PermissionResult.deniedPermissions: Set<Permission>
    inline get() = this.permissions.filter { entry -> !entry.value }.keys

/**
 * Determines if all the requested permissions have been granted.
 */
public inline fun PermissionResult.isGranted(): Boolean =
    this.permissions.all { entry -> entry.value }
