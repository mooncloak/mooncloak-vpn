package com.mooncloak.vpn.app.android

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.info.AppClientInfo

internal class AndroidAppClientInfo @Inject internal constructor() : AppClientInfo {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    // The BuildConfig.FLAVOR value is a constant that is determined at build-time. So, the condition here is valid since we need to check what the generated constant value is.
    @Suppress("KotlinConstantConditions")
    override val isGooglePlayBuild: Boolean
        get() = BuildConfig.FLAVOR == "play"

    override val flavor: String
        get() = BuildConfig.FLAVOR

    override val buildType: String
        get() = BuildConfig.BUILD_TYPE
}
