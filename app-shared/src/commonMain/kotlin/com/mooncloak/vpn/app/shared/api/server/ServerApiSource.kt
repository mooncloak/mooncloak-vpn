package com.mooncloak.vpn.app.shared.api.server

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.encode
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.api.shared.server.ServerRepository
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository

@OptIn(ExperimentalPaginationAPI::class)
public class ServerApiSource @Inject public constructor(
    private val serviceTokensRepository: ServiceTokensRepository,
    private val api: VpnServiceApi
) : ServerRepository {

    override suspend fun get(id: String): Server {
        val token = serviceTokensRepository.getLatest()?.accessToken

        return api.getServer(
            id = id,
            token = token
        )
    }

    override suspend fun get(count: Int, offset: Int): List<Server> {
        val token = serviceTokensRepository.getLatest()?.accessToken

        val cursor = Cursor.encode(
            offset = offset.toUInt(),
            count = count.toUInt()
        )

        return api.paginateServers(
            count = count.toUInt(),
            cursor = cursor,
            token = token
        ).items
    }

    override suspend fun get(query: String?, filters: ServerFilters, count: Int, offset: Int): List<Server> {
        val token = serviceTokensRepository.getLatest()?.accessToken

        val cursor = Cursor.encode(
            offset = offset.toUInt(),
            count = count.toUInt()
        )

        return api.paginateServers(
            query = query,
            filters = filters,
            count = count.toUInt(),
            cursor = cursor,
            token = token
        ).items
    }

    override suspend fun getAll(): List<Server> {
        TODO("Not yet implemented")
    }
}
