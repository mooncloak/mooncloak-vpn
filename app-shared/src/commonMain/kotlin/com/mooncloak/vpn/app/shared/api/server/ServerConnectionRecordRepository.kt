package com.mooncloak.vpn.app.shared.api.server

import kotlinx.coroutines.CancellationException
import kotlinx.datetime.Instant

public interface ServerConnectionRecordRepository {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): ServerConnectionRecord

    public suspend fun getLastConnected(): ServerConnectionRecord?

    public suspend fun getAll(): List<ServerConnectionRecord>

    public suspend fun getPage(
        count: Int = 20,
        offset: Int = 0
    ): List<ServerConnectionRecord>

    public suspend fun getAllStarred(): List<ServerConnectionRecord>

    public suspend fun getStarredPage(
        count: Int = 20,
        offset: Int = 0
    ): List<ServerConnectionRecord>

    public suspend fun add(
        server: Server,
        lastConnected: Instant? = null,
        starred: Boolean,
        note: String? = null
    )

    public suspend fun add(
        server: Server,
        lastConnected: Instant? = null,
        starred: Instant? = null,
        note: String? = null
    )

    public suspend fun update(
        server: Server,
        lastConnected: Instant? = null,
        starred: Instant?,
        note: String?
    )

    public suspend fun update(
        server: Server,
        lastConnected: Instant?
    )

    public suspend fun update(
        id: String,
        connected: Instant
    )

    public suspend fun update(
        id: String,
        starred: Boolean
    )

    public suspend fun update(
        id: String,
        note: String?
    )

    public suspend fun upsert(
        server: Server,
        lastConnected: Instant? = null
    ) {
        val model = getOrNull(id = server.id)

        if (model == null) {
            add(
                server = server,
                lastConnected = lastConnected,
                starred = null,
                note = null
            )
        } else {
            update(
                server = server,
                lastConnected = lastConnected
            )
        }
    }

    public suspend fun remove(id: String)

    public suspend fun clear()

    public companion object
}

public suspend fun ServerConnectionRecordRepository.getOrNull(id: String): ServerConnectionRecord? =
    try {
        get(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
