package com.mooncloak.vpn.app.desktop.api.server

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.util.coroutine.ApplicationCoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive

internal class JvmVPNConnectionManager @Inject internal constructor(
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
