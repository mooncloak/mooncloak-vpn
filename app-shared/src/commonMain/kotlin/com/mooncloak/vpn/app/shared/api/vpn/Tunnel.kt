package com.mooncloak.vpn.app.shared.api.vpn

import androidx.compose.runtime.Stable
import com.mooncloak.vpn.app.shared.api.server.Server

@Stable
public interface Tunnel {

    public val sessionId: String?
    public val tunnelName: String
    public val server: Server?
    public val stats: TunnelStats?

    public companion object
}
