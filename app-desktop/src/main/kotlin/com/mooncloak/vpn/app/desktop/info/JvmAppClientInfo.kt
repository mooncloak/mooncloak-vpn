package com.mooncloak.vpn.app.desktop.info

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.info.AppClientInfo

internal class JvmAppClientInfo @Inject internal constructor() : AppClientInfo {

    override val id: String = "com.mooncloak.vpn.app.desktop"

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
