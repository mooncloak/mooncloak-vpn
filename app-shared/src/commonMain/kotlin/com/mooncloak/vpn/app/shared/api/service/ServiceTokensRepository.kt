package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.data.shared.repository.MutableRepository

/**
 * An storage abstraction for accessing [ServiceTokens].
 */
public interface ServiceTokensRepository : MutableRepository<ServiceTokens> {

    /**
     * Retrieves the latest [ServiceTokens] instance.
     */
    public suspend fun getLatest(): ServiceTokens?

    /**
     * Adds the provided [ServiceTokens] instance to the underlying storage.
     */
    public suspend fun add(tokens: ServiceTokens) {
        this.insert(
            id = tokens.id,
            value = { tokens }
        )
    }

    public companion object
}
