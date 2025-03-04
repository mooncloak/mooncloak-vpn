package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.PresentationScoped

@PresentationScoped
public class ConnectToServerUseCase @Inject public constructor(
    private val vpnConnectionManager: VPNConnectionManager
) {

    public suspend operator fun invoke(server: Server): Boolean {
        vpnConnectionManager.connect(server)

        return true
    }
}
