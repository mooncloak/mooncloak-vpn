package com.mooncloak.vpn.util.permission

public actual class Permission public constructor(
    public val value: String
) {

    public actual companion object
}

public actual val Permission.Companion.PostNotification: Permission
    get() = postNotificationSingleton

private val postNotificationSingleton = Permission(value = "PostNotification")
