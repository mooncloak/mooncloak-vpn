package com.mooncloak.vpn.app.android

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.feature.app.AppClientInfo

internal class AndroidAppClientInfo @Inject internal constructor() : AppClientInfo {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override val flavor: String
        get() = BuildConfig.FLAVOR

    override val buildType: String
        get() = BuildConfig.BUILD_TYPE
}
