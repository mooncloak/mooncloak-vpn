@file:Suppress("MemberVisibilityCanBePrivate", "ConstPropertyName")

interface AppConstants {

    object Android {

        const val compileSdkVersion = 35
        const val minSdkVersion = 23
        const val targetSdkVersion = 35
    }

    object Ios {

        const val deploymentTarget: String = "17.0"
    }

    companion object : AppConstants
}
