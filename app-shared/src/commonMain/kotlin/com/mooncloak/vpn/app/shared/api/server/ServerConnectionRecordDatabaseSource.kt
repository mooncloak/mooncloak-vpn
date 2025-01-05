package com.mooncloak.vpn.app.shared.api.server

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.app.storage.sqlite.database.SelectAllStarred
import com.mooncloak.vpn.app.storage.sqlite.database.SelectStarredPage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

public class ServerConnectionRecordDatabaseSource @Inject public constructor(
    private val database: MooncloakDatabase,
    private val json: Json,
    private val clock: Clock
) : ServerConnectionRecordRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): ServerConnectionRecord =
        database.serverConnectionRecordQueries.selectById(id = id)
            .executeAsOneOrNull()
            ?.toServerConnectionRecord()
            ?: throw NoSuchElementException("No ServerConnectionRecord found with id '$id'.")

    override suspend fun getLastConnected(): ServerConnectionRecord? =
        database.serverConnectionRecordQueries.selectLastConnected()
            .executeAsOneOrNull()
            ?.toServerConnectionRecord()

    override suspend fun getAll(): List<ServerConnectionRecord> =
        database.serverConnectionRecordQueries.selectAll()
            .executeAsList()
            .map { record -> record.toServerConnectionRecord() }

    override suspend fun getPage(count: Int, offset: Int): List<ServerConnectionRecord> =
        database.serverConnectionRecordQueries.selectPage(
            count = count.toLong(),
            offset = offset.toLong()
        ).executeAsList()
            .map { record -> record.toServerConnectionRecord() }

    override suspend fun getAllStarred(): List<ServerConnectionRecord> =
        database.serverConnectionRecordQueries.selectAllStarred()
            .executeAsList()
            .map { record -> record.toServerConnectionRecord() }

    override suspend fun getStarredPage(count: Int, offset: Int): List<ServerConnectionRecord> =
        database.serverConnectionRecordQueries.selectStarredPage(
            count = count.toLong(),
            offset = offset.toLong()
        ).executeAsList()
            .map { record -> record.toServerConnectionRecord() }

    override suspend fun add(
        server: Server,
        lastConnected: Instant?,
        starred: Boolean,
        note: String?
    ) {
        add(
            server = server,
            lastConnected = lastConnected,
            starred = if (starred) {
                clock.now()
            } else {
                null
            },
            note = note
        )
    }

    override suspend fun add(server: Server, lastConnected: Instant?, starred: Instant?, note: String?) {
        mutex.withLock {
            val now = clock.now()

            database.serverConnectionRecordQueries.insert(
                databaseId = null,
                id = server.id,
                created = now,
                updated = now,
                serverCreated = server.created,
                serverUpdated = server.updated,
                connected = lastConnected,
                starred = starred,
                name = server.name,
                countryCode = server.country?.code?.value,
                regionCode = server.region?.code?.value,
                country = server.country?.let { country ->
                    json.encodeToJsonElement(
                        serializer = Country.serializer(),
                        value = country
                    )
                },
                region = server.region?.let { region ->
                    json.encodeToJsonElement(
                        serializer = Region.serializer(),
                        value = region
                    )
                },
                uri = server.uri,
                self = server.self,
                ipv4 = server.ipV4Address,
                ipv6 = server.ipV6Address,
                connectionTypes = json.encodeToJsonElement(
                    serializer = ListSerializer(ConnectionType.serializer()),
                    value = server.connectionTypes
                ),
                protocols = json.encodeToJsonElement(
                    serializer = ListSerializer(VPNProtocol.serializer()),
                    value = server.protocols
                ),
                tags = json.encodeToJsonElement(
                    serializer = ListSerializer(String.serializer()),
                    value = server.tags
                ),
                note = note,
                requiresSubscription = server.requiresSubscription
            )
        }
    }

    override suspend fun update(id: String, connected: Instant) {
        mutex.withLock {
            database.serverConnectionRecordQueries.updateConnectedById(
                id = id,
                connected = connected
            )
        }
    }

    override suspend fun update(id: String, starred: Boolean) {
        mutex.withLock {
            database.serverConnectionRecordQueries.updatedStarredById(
                id = id,
                starred = if (starred) {
                    clock.now()
                } else {
                    null
                }
            )
        }
    }

    override suspend fun update(id: String, note: String?) {
        mutex.withLock {
            database.serverConnectionRecordQueries.updateNoteById(
                id = id,
                note = note
            )
        }
    }

    override suspend fun remove(id: String) {
        mutex.withLock {
            database.serverConnectionRecordQueries.deleteById(id = id)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            database.serverConnectionRecordQueries.deleteAll()
        }
    }

    private fun com.mooncloak.vpn.app.storage.sqlite.database.ServerConnectionRecord.toServerConnectionRecord(): ServerConnectionRecord =
        ServerConnectionRecord(
            server = Server(
                id = this.id,
                name = this.name,
                country = this.country?.let { element ->
                    json.decodeFromJsonElement(
                        deserializer = Country.serializer(),
                        element = element
                    )
                },
                region = this.region?.let {
                    json.decodeFromJsonElement(
                        deserializer = Region.serializer(),
                        element = it
                    )
                },
                status = null,
                created = this.created,
                updated = this.updated,
                uri = this.uri,
                self = this.self,
                ipV4Address = this.ipv4,
                ipV6Address = this.ipv6,
                connectionTypes = json.decodeFromJsonElement(
                    deserializer = ListSerializer(ConnectionType.serializer()),
                    element = this.connectionTypes
                ),
                protocols = json.decodeFromJsonElement(
                    deserializer = ListSerializer(VPNProtocol.serializer()),
                    element = this.protocols
                ),
                tags = json.decodeFromJsonElement(
                    deserializer = ListSerializer(String.serializer()),
                    element = this.tags
                )
            )
        )

    private fun SelectAllStarred.toServerConnectionRecord(): ServerConnectionRecord =
        ServerConnectionRecord(
            server = Server(
                id = this.id,
                name = this.name,
                country = this.country?.let { element ->
                    json.decodeFromJsonElement(
                        deserializer = Country.serializer(),
                        element = element
                    )
                },
                region = this.region?.let {
                    json.decodeFromJsonElement(
                        deserializer = Region.serializer(),
                        element = it
                    )
                },
                status = null,
                created = this.created,
                updated = this.updated,
                uri = this.uri,
                self = this.self,
                ipV4Address = this.ipv4,
                ipV6Address = this.ipv6,
                connectionTypes = json.decodeFromJsonElement(
                    deserializer = ListSerializer(ConnectionType.serializer()),
                    element = this.connectionTypes
                ),
                protocols = json.decodeFromJsonElement(
                    deserializer = ListSerializer(VPNProtocol.serializer()),
                    element = this.protocols
                ),
                tags = json.decodeFromJsonElement(
                    deserializer = ListSerializer(String.serializer()),
                    element = this.tags
                )
            )
        )

    private fun SelectStarredPage.toServerConnectionRecord(): ServerConnectionRecord =
        ServerConnectionRecord(
            server = Server(
                id = this.id,
                name = this.name,
                country = this.country?.let { element ->
                    json.decodeFromJsonElement(
                        deserializer = Country.serializer(),
                        element = element
                    )
                },
                region = this.region?.let {
                    json.decodeFromJsonElement(
                        deserializer = Region.serializer(),
                        element = it
                    )
                },
                status = null,
                created = this.created,
                updated = this.updated,
                uri = this.uri,
                self = this.self,
                ipV4Address = this.ipv4,
                ipV6Address = this.ipv6,
                connectionTypes = json.decodeFromJsonElement(
                    deserializer = ListSerializer(ConnectionType.serializer()),
                    element = this.connectionTypes
                ),
                protocols = json.decodeFromJsonElement(
                    deserializer = ListSerializer(VPNProtocol.serializer()),
                    element = this.protocols
                ),
                tags = json.decodeFromJsonElement(
                    deserializer = ListSerializer(String.serializer()),
                    element = this.tags
                )
            )
        )
}
