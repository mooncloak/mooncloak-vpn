package com.mooncloak.vpn.api.shared.server

import com.mooncloak.vpn.api.shared.key.Base64Key
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

public interface RegisteredClientRepository {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): RegisteredClient

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getByServerId(
        serverId: String
    ): RegisteredClient

    public suspend fun getAll(): List<RegisteredClient>

    public suspend fun insert(
        tokenId: String? = null,
        protocol: VPNProtocol,
        clientId: String? = null,
        registered: Instant,
        expiration: Instant? = null,
        publicKey: Base64Key,
        publicKeyId: String? = null,
        allowedIpAddresses: List<String>? = null,
        persistentKeepAlive: Int? = null,
        endpoint: String? = null,
        serverId: String,
        assignedAddress: String
    ): RegisteredClient

    public suspend fun remove(id: String)

    public suspend fun clear()

    public companion object
}

public suspend fun RegisteredClientRepository.getOrNull(id: String): RegisteredClient? =
    try {
        get(id = id)
    } catch (_: NoSuchElementException) {
        null
    }

public suspend fun RegisteredClientRepository.getByServerIdOrNull(
    serverId: String
): RegisteredClient? =
    try {
        getByServerId(serverId = serverId)
    } catch (_: NoSuchElementException) {
        null
    }
