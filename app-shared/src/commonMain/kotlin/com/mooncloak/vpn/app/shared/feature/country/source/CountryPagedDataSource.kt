package com.mooncloak.vpn.app.shared.feature.country.source

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.PageInfo
import com.mooncloak.kodetools.pagex.PageLoadResult
import com.mooncloak.kodetools.pagex.PageRequest
import com.mooncloak.kodetools.pagex.PagedDataSource
import com.mooncloak.kodetools.pagex.emptyPage
import com.mooncloak.kodetools.pagex.page
import com.mooncloak.vpn.api.shared.location.CountryFilters
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.location.CountryDetails
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalPaginationAPI::class)
public class CountryPagedDataSource @Inject public constructor(
    private val mooncloakVpnServiceHttpApi: VpnServiceApi
) : PagedDataSource<String, CountryFilters, CountryDetails> {

    @OptIn(ExperimentalUuidApi::class)
    override val sourceId: String = Uuid.random().toHexString()

    override suspend fun load(request: PageRequest<String, CountryFilters>): PageLoadResult<CountryDetails> =
        try {
            val page = mooncloakVpnServiceHttpApi.paginateCountries(
                direction = request.direction,
                cursor = request.cursor,
                count = request.count,
                sort = request.sort,
                filters = request.filters
            )

            PageLoadResult.page(
                items = page.countries,
                info = PageInfo(
                    hasPrevious = page.info.hasPrevious,
                    hasNext = page.info.hasNext,
                    firstCursor = page.info.firstCursor,
                    lastCursor = page.info.lastCursor,
                    totalCount = page.info.totalCount?.toUInt()
                )
            )
        } catch (e: Exception) {
            LogPile.error(message = "Error loading countries.", cause = e)

            PageLoadResult.emptyPage()
        }
}
