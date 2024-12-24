package com.mooncloak.vpn.app.android

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow

public class AndroidServerConnectionManager @Inject public constructor(

) : ServerConnectionManager {

    override val connection: SharedFlow<ServerConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow(ServerConnection.Disconnected)

    override suspend fun connect(server: Server) {
        // TODO:
    }

    override suspend fun disconnect() {
        // TODO:
    }
}
