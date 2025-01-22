package com.mooncloak.vpn.app.shared.util.permission

import android.Manifest
import android.annotation.TargetApi
import android.os.Build

public actual class Permission public constructor(
    public val value: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Permission) return false

        return value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = "Permission(value='$value')"

    public actual companion object
}

public actual val Permission.Companion.PostNotification: Permission
    @TargetApi(Build.VERSION_CODES.TIRAMISU)
    get() = Permission(value = Manifest.permission.POST_NOTIFICATIONS)
