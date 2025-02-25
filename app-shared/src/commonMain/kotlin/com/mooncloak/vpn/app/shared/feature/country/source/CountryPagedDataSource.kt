package com.mooncloak.vpn.app.shared.feature.country.source

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.PageLoadResult
import com.mooncloak.kodetools.pagex.PageRequest
import com.mooncloak.kodetools.pagex.PagedDataSource
import com.mooncloak.kodetools.pagex.emptyPage
import com.mooncloak.vpn.api.shared.location.CountryFilters
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalPaginationAPI::class)
public class CountryPagedDataSource @Inject public constructor(
    private val mooncloakVpnServiceHttpApi: MooncloakVpnServiceHttpApi
) : PagedDataSource<String, CountryFilters, Country> {

    @OptIn(ExperimentalUuidApi::class)
    override val sourceId: String = Uuid.random().toHexString()

    override suspend fun load(request: PageRequest<String, CountryFilters>): PageLoadResult<Country> =
        try {
            mooncloakVpnServiceHttpApi.paginateCountries(
                direction = request.direction,
                cursor = request.cursor,
                count = request.count,
                sort = request.sort,
                filters = request.filters
            )
        } catch (e: Exception) {
            LogPile.error(message = "Error loading countries.", cause = e)

            PageLoadResult.emptyPage()
        }
}
