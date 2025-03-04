package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.getOrNull
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPaginationAPI::class)
public class GetDefaultServerUseCase @Inject public constructor(
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val api: VpnServiceApi
) {

    public suspend operator fun invoke(): Server? =
        withContext(Dispatchers.IO) {
            serverConnectionRecordRepository.getLastConnected()?.server?.let { return@withContext it }

            serverConnectionRecordRepository.getStarred().firstOrNull()?.server?.let { return@withContext it }

            return@withContext api.paginateServers().getOrNull()?.firstOrNull()
        }
}
