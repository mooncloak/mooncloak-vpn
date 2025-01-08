package com.mooncloak.vpn.app.shared.api.vpn

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.VPNConnectionStatus

@Stable
public interface Tunnel {

    public val sessionId: String?
    public val tunnelName: String
    public val server: Server?

    public val stats: State<TunnelStats?>
    public val status: State<VPNConnectionStatus>

    public companion object
}
