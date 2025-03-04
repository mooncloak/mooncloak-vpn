package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.LocationCode
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.api.shared.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.shared.di.PresentationScoped

@PresentationScoped
public class ConnectToServerInLocationCodeUseCase @Inject public constructor(
    private val api: VpnServiceApi,
    private val vpnConnectionManager: VPNConnectionManager
) {

    @OptIn(ExperimentalPaginationAPI::class)
    public suspend operator fun invoke(locationCode: LocationCode): Boolean {
        val server = api.paginateServers(
            filters = ServerFilters(
                locationCode = locationCode
            )
        ).items.firstOrNull()
            ?: return false

        vpnConnectionManager.connect(server)

        return true
    }
}
