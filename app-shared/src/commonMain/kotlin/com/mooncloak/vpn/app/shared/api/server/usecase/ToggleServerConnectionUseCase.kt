package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.network.core.vpn.connectedTo
import com.mooncloak.vpn.app.shared.di.PresentationScoped

@PresentationScoped
public class ToggleServerConnectionUseCase @Inject public constructor(
    private val vpnConnectionManager: VPNConnectionManager
) {

    public suspend operator fun invoke(server: Server): Boolean {
        if (vpnConnectionManager.connection.value.connectedTo(server)) {
            vpnConnectionManager.disconnect()
        } else {
            vpnConnectionManager.connect(server)
        }

        return true
    }
}
