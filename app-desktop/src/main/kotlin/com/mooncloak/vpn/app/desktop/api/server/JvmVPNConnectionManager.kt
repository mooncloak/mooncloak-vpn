package com.mooncloak.vpn.app.desktop.api.server

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnection
import com.mooncloak.vpn.app.shared.api.vpn.VPNConnectionManager
import kotlinx.coroutines.flow.StateFlow

internal class JvmVPNConnectionManager @Inject internal constructor(

) : VPNConnectionManager {

    override val isActive: Boolean
        get() = TODO("Not yet implemented")

    override val connection: StateFlow<VPNConnection>
        get() = TODO("Not yet implemented")

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
