package com.mooncloak.vpn.app.shared.api.service

import kotlinx.coroutines.CancellationException

public interface ServiceTokensRepository {

    public suspend fun getLatest(): ServiceTokens?

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): ServiceTokens

    public suspend fun add(tokens: ServiceTokens)

    public suspend fun remove(id: String)

    public companion object
}

public suspend fun ServiceTokensRepository.getOrNull(id: String): ServiceTokens? =
    try {
        get(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
