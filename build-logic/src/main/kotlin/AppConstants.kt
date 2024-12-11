@file:Suppress("MemberVisibilityCanBePrivate")

interface AppConstants {

    val name: String get() = "mooncloak VPN"

    val description get() = "Privacy focused apps"

    object Android {

        const val compileSdkVersion = 35
        const val minSdkVersion = 23
        const val targetSdkVersion = 35
    }

    companion object : AppConstants
}
