package com.mooncloak.vpn.app.shared.api.server

import kotlinx.coroutines.flow.SharedFlow

public interface ServerConnectionManager {

    public val connection: SharedFlow<ServerConnection>

    public suspend fun connect(server: Server)

    public suspend fun disconnect()

    public companion object
}
