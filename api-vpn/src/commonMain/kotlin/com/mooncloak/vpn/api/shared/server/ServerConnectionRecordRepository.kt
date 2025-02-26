package com.mooncloak.vpn.api.shared.server

import com.mooncloak.vpn.data.shared.repository.MutableRepository

public interface ServerConnectionRecordRepository : MutableRepository<ServerConnectionRecord> {

    public suspend fun getLastConnected(): ServerConnectionRecord?

    public suspend fun getAllStarred(): List<ServerConnectionRecord>

    public suspend fun getStarred(
        count: Int = 20,
        offset: Int = 0
    ): List<ServerConnectionRecord>

    public companion object
}
