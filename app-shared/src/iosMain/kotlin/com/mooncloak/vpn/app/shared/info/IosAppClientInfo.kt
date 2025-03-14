package com.mooncloak.vpn.app.shared.info

public operator fun AppClientInfo.Companion.invoke(): AppClientInfo = IosAppClientInfo()

internal class IosAppClientInfo internal constructor() : AppClientInfo {

    override val id: String = "com.mooncloak.vpn.app-ios"

    override val isDebug: Boolean = false // FIXME: JVM isDebug

    override val isPreRelease: Boolean =
        version.contains("dev") ||
                version.contains("alpha") ||
                version.contains("beta") ||
                version.contains("test") ||
                version.contains("int") ||
                version.contains("stage") ||
                version.contains("pre")

    override val isGooglePlayBuild: Boolean = false

    override val flavor: String? = null

    override val buildType: String = "release" // FIXME: JVM buildType
}
