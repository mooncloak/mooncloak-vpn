package com.mooncloak.vpn.app.desktop

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.feature.app.AppClientInfo

internal class DesktopAppClientInfo @Inject internal constructor() : AppClientInfo {

    override val isDebug: Boolean
        get() = false // TODO: FIXME

    override val flavor: String? = null

    override val buildType: String? = null
}
