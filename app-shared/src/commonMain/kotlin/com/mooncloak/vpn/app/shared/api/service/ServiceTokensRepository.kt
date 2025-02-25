package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.vpn.api.shared.service.ServiceTokens
import kotlinx.coroutines.CancellationException

/**
 * An storage abstraction for accessing [ServiceTokens].
 */
public interface ServiceTokensRepository {

    /**
     * Retrieves the latest [ServiceTokens] instance.
     */
    public suspend fun getLatest(): ServiceTokens?

    /**
     * Retrieves the [ServiceTokens] with the provided [id], or throws a [NoSuchElementException] if there is no
     * [ServiceTokens] with the provided [id].
     *
     * @param [id] The [ServiceTokens.id] value of the [ServiceTokens] to retrieve.
     *
     * @throws [NoSuchElementException] if no [ServiceTokens] exists in the underlying storage with the provided [id].
     *
     * @return The [ServiceTokens] instance that has the provided [id] value.
     */
    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): ServiceTokens

    /**
     * Adds the provided [ServiceTokens] instance to the underlying storage.
     */
    public suspend fun add(tokens: ServiceTokens)

    /**
     * Removes the [ServiceTokens] containing the provided [id] from the underlying storage if it exists.
     */
    public suspend fun remove(id: String)

    /**
     * Clears all the [ServiceTokens] in the underlying storage.
     */
    public suspend fun clear()

    public companion object
}

/**
 * Retrieves the [ServiceTokens] with the provided [id], or `null` if there is no [ServiceTokens] with the provided
 * [id].
 *
 * @param [id] The [ServiceTokens.id] value of the [ServiceTokens] to retrieve.
 *
 * @return The [ServiceTokens] instance that has the provided [id] value, or `null` if no [ServiceTokens] exists in the
 * underlying storage with the provided [id].
 */
public suspend fun ServiceTokensRepository.getOrNull(id: String): ServiceTokens? =
    try {
        get(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
