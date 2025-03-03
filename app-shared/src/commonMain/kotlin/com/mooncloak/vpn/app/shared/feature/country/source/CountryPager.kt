package com.mooncloak.vpn.app.shared.feature.country.source

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.PageRequest
import com.mooncloak.kodetools.pagex.Pager
import com.mooncloak.kodetools.pagex.create
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.api.shared.location.CountryFilters
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalPaginationAPI::class)
public typealias CountryPager = Pager<CountryDetails>

@OptIn(ExperimentalPaginationAPI::class)
public class CountryPagerLoader @Inject public constructor(
    dataSource: CountryPagedDataSource
) : Pager.Loader<String, CountryFilters, CountryDetails> {

    private val delegateLoader = Pager.Loader.create(dataSource)

    override fun load(
        request: PageRequest<String, CountryFilters>,
        coroutineScope: CoroutineScope
    ): Pager<CountryDetails> = delegateLoader.load(
        request = request,
        coroutineScope = coroutineScope
    )
}
