@file:Suppress("MemberVisibilityCanBePrivate")

interface AppConstants {

    val name: String get() = "mooncloak"

    val description get() = "Privacy focused apps"

    object Android {

        const val compileSdkVersion = 34
        const val minSdkVersion = 23
        const val targetSdkVersion = 34
    }

    companion object : AppConstants
}
