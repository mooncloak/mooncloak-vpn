package com.mooncloak.vpn.app.shared.api.server

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.key.Base64Key
import com.mooncloak.vpn.api.shared.server.RegisteredClient
import com.mooncloak.vpn.api.shared.server.RegisteredClientRepository
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class RegisteredClientDatabaseSource @Inject public constructor(
    private val database: MooncloakDatabase,
    private val clock: Clock,
    private val json: Json
) : RegisteredClientRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): RegisteredClient {
        val model = database.registeredClientQueries.selectById(id = id).executeAsOneOrNull()
            ?: throw NoSuchElementException("No RegisteredClient found with id '$id'.")

        return model.toRegisteredPeer()
    }

    override suspend fun getByServerId(serverId: String): RegisteredClient {
        val model = database.registeredClientQueries.selectByServerId(
            serverId = serverId
        ).executeAsOneOrNull()
            ?: throw NoSuchElementException("No RegisteredClient found with serverId '$serverId'.")

        return model.toRegisteredPeer()
    }

    override suspend fun getAll(): List<RegisteredClient> {
        return database.registeredClientQueries.selectAll()
            .executeAsList()
            .map { model -> model.toRegisteredPeer() }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun insert(
        tokenId: String?,
        protocol: VPNProtocol,
        clientId: String?,
        registered: Instant,
        expiration: Instant?,
        publicKey: Base64Key,
        publicKeyId: String?,
        allowedIpAddresses: List<String>?,
        persistentKeepAlive: Int?,
        endpoint: String?,
        serverId: String,
        assignedAddress: String
    ): RegisteredClient = mutex.withLock {
        val now = clock.now()
        val id = Uuid.random().toHexString()

        database.registeredClientQueries.insert(
            id = id,
            created = now,
            updated = now,
            registered = now,
            expiration = expiration,
            client_id = clientId,
            public_key = publicKey.value,
            public_key_id = publicKeyId,
            allowed_ips = allowedIpAddresses?.let { ips ->
                json.encodeToJsonElement(
                    serializer = ListSerializer(String.serializer()),
                    value = ips
                )
            },
            persistent_keep_alive = persistentKeepAlive?.toLong(),
            endpoint = endpoint,
            token_id = tokenId,
            server_id = serverId,
            protocol = protocol.value,
            assigned_address = assignedAddress
        )

        return RegisteredClient(
            id = id,
            clientId = clientId,
            created = now,
            updated = now,
            registered = now,
            expiration = expiration,
            publicKey = publicKey,
            publicKeyId = publicKeyId,
            allowedIpAddresses = allowedIpAddresses,
            persistentKeepAlive = persistentKeepAlive,
            endpoint = endpoint,
            tokenId = tokenId,
            serverId = serverId,
            protocol = protocol,
            assignedAddress = assignedAddress
        )
    }

    override suspend fun remove(id: String) {
        mutex.withLock {
            database.registeredClientQueries.deleteById(id = id)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            database.registeredClientQueries.deleteAll()
        }
    }

    private fun com.mooncloak.vpn.app.storage.sqlite.database.RegisteredClient.toRegisteredPeer(): RegisteredClient =
        RegisteredClient(
            id = this.id,
            clientId = this.client_id,
            created = this.created,
            updated = this.updated,
            registered = this.registered,
            expiration = this.expiration,
            publicKey = Base64Key(value = this.public_key),
            publicKeyId = this.public_key_id,
            allowedIpAddresses = this.allowed_ips?.let { element ->
                json.decodeFromJsonElement(
                    deserializer = ListSerializer(String.serializer()),
                    element = element
                )
            },
            persistentKeepAlive = this.persistent_keep_alive?.toInt(),
            endpoint = this.endpoint,
            tokenId = this.token_id,
            serverId = this.server_id,
            protocol = VPNProtocol(value = this.protocol),
            preSharedKey = null,
            assignedAddress = this.assigned_address
        )
}
