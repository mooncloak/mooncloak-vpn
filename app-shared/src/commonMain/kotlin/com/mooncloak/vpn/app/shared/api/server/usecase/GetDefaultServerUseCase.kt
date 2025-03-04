package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.api.shared.server.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class GetDefaultServerUseCase @Inject public constructor(
    private val serverConnectionRecordRepository: ServerConnectionRecordRepository,
    private val serverRepository: ServerRepository
) {

    public suspend operator fun invoke(): Server? =
        withContext(Dispatchers.IO) {
            serverConnectionRecordRepository.getLastConnected()?.server?.let { return@withContext it }

            serverConnectionRecordRepository.getStarred().firstOrNull()?.server?.let { return@withContext it }

            return@withContext serverRepository.get().firstOrNull()
        }
}
