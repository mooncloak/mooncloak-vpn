package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.LocationCode
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.api.shared.server.ServerRepository
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager

public class ConnectToServerInLocationCodeUseCase @Inject public constructor(
    private val serverRepository: ServerRepository,
    private val vpnConnectionManager: VPNConnectionManager
) {

    public suspend operator fun invoke(locationCode: LocationCode): Boolean {
        val server = serverRepository.get(
            filters = ServerFilters(
                locationCode = locationCode
            )
        ).firstOrNull()
            ?: return false

        vpnConnectionManager.connect(server)

        return true
    }
}
