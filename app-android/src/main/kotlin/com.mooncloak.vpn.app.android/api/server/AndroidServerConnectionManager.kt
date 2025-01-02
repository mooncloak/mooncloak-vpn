package com.mooncloak.vpn.app.android.api.server

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerConnection
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionManager
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.wireguard.android.backend.GoBackend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(ExperimentalPersistentStateAPI::class)
public class AndroidServerConnectionManager @Inject public constructor(
    context: ApplicationContext,
    private val api: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage,
) : ServerConnectionManager {

    override val connection: StateFlow<ServerConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow(ServerConnection.Disconnected)

    private val backend = GoBackend(context)

    override suspend fun connect(server: Server) {
        val serverPublicKey = api.registerClient(
            clientPublicKey = "", // TODO: Generate client keypair
            token = subscriptionStorage.tokens.current.value?.accessToken
                ?: error("Not authorized. Must be authorized to connect to a VPN server.")
        ).serverPublicKey

        // TODO:
    }

    override suspend fun disconnect() {
        backend.version
        // TODO:
    }
}
