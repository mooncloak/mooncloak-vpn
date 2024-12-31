package com.mooncloak.vpn.app.shared.api.server

import kotlinx.coroutines.flow.StateFlow

public interface ServerConnectionManager {

    public val connection: StateFlow<ServerConnection>

    public suspend fun connect(server: Server)

    public suspend fun disconnect()

    public companion object
}
