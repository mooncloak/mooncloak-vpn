package com.mooncloak.vpn.api.shared.provider

import com.mooncloak.vpn.data.shared.provider.Provider
import io.ktor.http.Url

/**
 * Provides the base host URL for an API service.
 *
 * Implementations determine the base URL dynamically, based on environment, configuration, or other factors. Supports
 * asynchronous retrieval.
 */
public fun interface HostUrlProvider : Provider<Url> {

    public override suspend fun get(): Url

    public companion object
}

/**
 * Retrieve the production API [HostUrlProvider] for mooncloak.
 */
public val HostUrlProvider.Companion.Mooncloak: HostUrlProvider
    get() = MooncloakProductionApiHostUrlProvider

internal data object MooncloakProductionApiHostUrlProvider : HostUrlProvider {

    override suspend fun get(): Url = Url("https://mooncloak.com/api")
}
