package com.mooncloak.vpn.app.android

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalPersistentStateAPI::class)
public class AndroidServerConnectionManager @Inject public constructor(
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage
) : ServerConnectionManager {

    override val connection: SharedFlow<ServerConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow(ServerConnection.Disconnected)

    override suspend fun connect(server: Server) {
        val serverPublicKey = api.registerClient(
            serverId = server.id,
            clientPublicKey = "", // TODO: Generate client keypair
            token = subscriptionStorage.tokens.current.value?.accessToken
                ?: error("Not authorized. Must be authorized to connect to a VPN server.")
        ).serverPublicKey

        // TODO:
    }

    override suspend fun disconnect() {
        // TODO:
    }
}
