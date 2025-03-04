package com.mooncloak.vpn.api.shared.server

import com.mooncloak.vpn.data.shared.repository.Repository

public interface ServerRepository : Repository<Server> {

    /**
     * Paginates through filtered [Server]s.
     *
     * @param [query] An optional query [String] to filter [Server]s.
     *
     * @param [filters] The [ServerFilters] used to filter through the [Server]s.
     *
     * @param [count] The amount of items to retrieve. Defaults to 25.
     *
     * @param [offset] The item offset. Defaults to zero.
     *
     * @return The filtered [Server]s.
     */
    public suspend fun get(
        query: String? = null,
        filters: ServerFilters,
        count: Int = 25,
        offset: Int = 0
    ): List<Server>

    public companion object
}
