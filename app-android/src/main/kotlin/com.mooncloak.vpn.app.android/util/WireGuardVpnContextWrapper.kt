package com.mooncloak.vpn.app.android.util

import android.content.ComponentName
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import com.mooncloak.vpn.app.android.service.MooncloakVpnService
import com.wireguard.android.backend.GoBackend
import com.wireguard.android.backend.GoBackend.VpnService

/**
 * This wraps a [Context] and overrides some of its functionality related to the WireGuard VPN service. The Android
 * WireGuard library that we use is not very extensible and makes it difficult to provide additional functionality,
 * such as starting the VPN service as a foreground service (which is required by Android for VPN services). So, we
 * have to provide this [WireGuardVpnContextWrapper] as the [Context] when creating a [GoBackend] WireGuard component
 * so that we can intercept when they launch their VPN Service, and instead launch our VPN Service which extends
 * theirs. This solution is a bit hacky but unfortunately is currently required. Eventually we can wrap the WireGuard
 * Go library ourselves in a Kotlin multiplatform library and make it much more flexible.
 */
public class WireGuardVpnContextWrapper public constructor(
    base: Context
) : ContextWrapper(base) {

    override fun startService(service: Intent?): ComponentName? {
        // Safety/sanity check that the service being started is in fact the GoBackend.VpnService. This
        // WireGuardVpnContextWrapper component should only be used for the GoBackend, but we still check just in case.
        return if (service?.component?.className == VpnService::class.java.name) {
            val mooncloakServiceIntent = Intent(this, MooncloakVpnService::class.java)

            if (Build.VERSION.SDK_INT >= 26) {
                baseContext.startForegroundService(mooncloakServiceIntent)
            } else {
                baseContext.startService(mooncloakServiceIntent)
            }
        } else {
            super.startService(service)
        }
    }
}
