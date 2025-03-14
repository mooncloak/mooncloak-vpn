package com.mooncloak.vpn.app.shared.api.server

import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.vpn.VPNConnection
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive

public operator fun VPNConnectionManager.Companion.invoke(
    coroutineScope: ApplicationCoroutineScope
): VPNConnectionManager = IosVPNConnectionManager(coroutineScope = coroutineScope)

internal class IosVPNConnectionManager internal constructor(
    private val coroutineScope: ApplicationCoroutineScope
) : VPNConnectionManager {

    override val isActive: Boolean
        get() = coroutineScope.isActive

    override val connection: StateFlow<VPNConnection>
        get() = mutableConnection.asStateFlow()

    private val mutableConnection = MutableStateFlow<VPNConnection>(VPNConnection.Disconnected())

    override fun start() {
    }

    override fun cancel() {
    }

    override suspend fun connect(server: Server) {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }
}
