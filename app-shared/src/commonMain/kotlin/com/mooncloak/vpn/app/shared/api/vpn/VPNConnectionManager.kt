package com.mooncloak.vpn.app.shared.api.vpn

import com.mooncloak.vpn.app.shared.api.server.Server
import kotlinx.coroutines.flow.StateFlow

public interface VPNConnectionManager {

    public val connection: StateFlow<VPNConnection>

    public suspend fun connect(server: Server)

    public suspend fun disconnect()

    public companion object
}
