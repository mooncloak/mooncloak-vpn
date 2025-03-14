package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.PresentationScoped

@PresentationScoped
public class ConnectToDefaultServerUseCase @Inject public constructor(
    private val getDefaultServer: GetDefaultServerUseCase,
    private val vpnConnectionManager: VPNConnectionManager
) {

    public suspend operator fun invoke(): Boolean {
        val server = getDefaultServer() ?: return false

        vpnConnectionManager.connect(server)

        return true
    }
}
