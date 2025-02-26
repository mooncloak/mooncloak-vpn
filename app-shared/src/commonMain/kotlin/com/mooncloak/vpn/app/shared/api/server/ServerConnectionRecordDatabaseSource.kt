package com.mooncloak.vpn.app.shared.api.server

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.locale.Region
import com.mooncloak.vpn.api.shared.server.ConnectionType
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecord
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import com.mooncloak.vpn.app.shared.database.MooncloakDatabaseProvider
import com.mooncloak.vpn.data.sqlite.database.SelectAllStarred
import com.mooncloak.vpn.data.sqlite.database.SelectStarredPage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

public class ServerConnectionRecordDatabaseSource @Inject public constructor(
    private val databaseProvider: MooncloakDatabaseProvider,
    private val json: Json,
    private val clock: Clock
) : ServerConnectionRecordRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): ServerConnectionRecord {
        val database = databaseProvider.get()

        return database.serverConnectionRecordQueries.selectById(id = id)
            .executeAsOneOrNull()
            ?.toServerConnectionRecord()
            ?: throw NoSuchElementException("No ServerConnectionRecord found with id '$id'.")
    }

    override suspend fun getLastConnected(): ServerConnectionRecord? {
        val database = databaseProvider.get()

        return database.serverConnectionRecordQueries.selectLastConnected()
            .executeAsOneOrNull()
            ?.toServerConnectionRecord()
    }

    override suspend fun getAll(): List<ServerConnectionRecord> {
        val database = databaseProvider.get()

        return database.serverConnectionRecordQueries.selectAll()
            .executeAsList()
            .map { record -> record.toServerConnectionRecord() }
    }

    override suspend fun get(count: Int, offset: Int): List<ServerConnectionRecord> {
        val database = databaseProvider.get()

        return database.serverConnectionRecordQueries.selectPage(
            count = count.toLong(),
            offset = offset.toLong()
        ).executeAsList()
            .map { record -> record.toServerConnectionRecord() }
    }

    override suspend fun getAllStarred(): List<ServerConnectionRecord> {
        val database = databaseProvider.get()

        return database.serverConnectionRecordQueries.selectAllStarred()
            .executeAsList()
            .map { record -> record.toServerConnectionRecord() }
    }

    override suspend fun getStarred(count: Int, offset: Int): List<ServerConnectionRecord> {
        val database = databaseProvider.get()

        return database.serverConnectionRecordQueries.selectStarredPage(
            count = count.toLong(),
            offset = offset.toLong()
        ).executeAsList()
            .map { record -> record.toServerConnectionRecord() }
    }

    override suspend fun insert(id: String, value: () -> ServerConnectionRecord): ServerConnectionRecord =
        mutex.withLock {
            val database = databaseProvider.get()
            val now = clock.now()
            val model = value.invoke()

            database.transactionWithResult {
                database.serverConnectionRecordQueries.insert(
                    databaseId = null,
                    id = model.server.id,
                    created = now,
                    updated = now,
                    serverCreated = model.server.created,
                    serverUpdated = model.server.updated,
                    connected = model.lastConnected,
                    starred = model.starred,
                    name = model.server.name,
                    countryCode = model.server.country?.code?.value,
                    regionCode = model.server.region?.code?.value,
                    country = model.server.country?.let { country ->
                        json.encodeToJsonElement(
                            serializer = Country.serializer(),
                            value = country
                        )
                    },
                    region = model.server.region?.let { region ->
                        json.encodeToJsonElement(
                            serializer = Region.serializer(),
                            value = region
                        )
                    },
                    uri = model.server.uri,
                    self = model.server.self,
                    ipv4 = model.server.ipV4Address,
                    ipv6 = model.server.ipV6Address,
                    hostname = model.server.hostname,
                    port = model.server.port?.toLong(),
                    connectionTypes = json.encodeToJsonElement(
                        serializer = ListSerializer(ConnectionType.serializer()),
                        value = model.server.connectionTypes
                    ),
                    protocols = json.encodeToJsonElement(
                        serializer = ListSerializer(VPNProtocol.serializer()),
                        value = model.server.protocols
                    ),
                    tags = json.encodeToJsonElement(
                        serializer = ListSerializer(String.serializer()),
                        value = model.server.tags
                    ),
                    note = model.note,
                    requiresSubscription = model.server.requiresSubscription,
                    publicKey = model.server.publicKey
                )

                database.serverConnectionRecordQueries.selectById(id = model.server.id)
                    .executeAsOne()
                    .toServerConnectionRecord()
            }
        }

    override suspend fun update(
        id: String,
        update: ServerConnectionRecord.() -> ServerConnectionRecord
    ): ServerConnectionRecord =
        mutex.withLock {
            val database = databaseProvider.get()
            val now = clock.now()

            database.transactionWithResult {
                val current = database.serverConnectionRecordQueries.selectById(id = id)
                    .executeAsOneOrNull()
                    ?.toServerConnectionRecord()
                    ?: throw NoSuchElementException("No '${ServerConnectionRecord::class.simpleName}' with id '$id'.")
                val updated = current.update()

                require(updated.server.id == id) {
                    "ID '$id' does not match server id '${updated.server.id}'."
                }

                database.serverConnectionRecordQueries.updateAll(
                    id = updated.server.id,
                    updated = now,
                    serverUpdated = updated.server.updated,
                    connected = updated.lastConnected,
                    starred = updated.starred,
                    name = updated.server.name,
                    countryCode = updated.server.country?.code?.value,
                    regionCode = updated.server.region?.code?.value,
                    country = updated.server.country?.let { country ->
                        json.encodeToJsonElement(
                            serializer = Country.serializer(),
                            value = country
                        )
                    },
                    region = updated.server.region?.let { region ->
                        json.encodeToJsonElement(
                            serializer = Region.serializer(),
                            value = region
                        )
                    },
                    uri = updated.server.uri,
                    self = updated.server.self,
                    ipv4 = updated.server.ipV4Address,
                    ipv6 = updated.server.ipV6Address,
                    hostname = updated.server.hostname,
                    port = updated.server.port?.toLong(),
                    connectionTypes = json.encodeToJsonElement(
                        serializer = ListSerializer(ConnectionType.serializer()),
                        value = updated.server.connectionTypes
                    ),
                    protocols = json.encodeToJsonElement(
                        serializer = ListSerializer(VPNProtocol.serializer()),
                        value = updated.server.protocols
                    ),
                    tags = json.encodeToJsonElement(
                        serializer = ListSerializer(String.serializer()),
                        value = updated.server.tags
                    ),
                    note = updated.note,
                    requiresSubscription = updated.server.requiresSubscription,
                    publicKey = updated.server.publicKey
                )

                updated
            }
        }

    override suspend fun remove(id: String) {
        mutex.withLock {
            val database = databaseProvider.get()

            database.serverConnectionRecordQueries.deleteById(id = id)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            val database = databaseProvider.get()

            database.serverConnectionRecordQueries.deleteAll()
        }
    }

    private fun com.mooncloak.vpn.data.sqlite.database.ServerConnectionRecord.toServerConnectionRecord(): ServerConnectionRecord =
        ServerConnectionRecord(
            lastConnected = this.connected,
            starred = this.starred,
            note = this.note,
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
                hostname = this.hostname,
                port = this.port?.toInt(),
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
                ),
                publicKey = this.publicKey
            )
        )

    private fun SelectAllStarred.toServerConnectionRecord(): ServerConnectionRecord =
        ServerConnectionRecord(
            lastConnected = this.connected,
            starred = this.starred,
            note = this.note,
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
                hostname = this.hostname,
                port = this.port?.toInt(),
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
                ),
                publicKey = this.publicKey
            )
        )

    private fun SelectStarredPage.toServerConnectionRecord(): ServerConnectionRecord =
        ServerConnectionRecord(
            lastConnected = this.connected,
            starred = this.starred,
            note = this.note,
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
                hostname = this.hostname,
                port = this.port?.toInt(),
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
                ),
                publicKey = this.publicKey
            )
        )
}
