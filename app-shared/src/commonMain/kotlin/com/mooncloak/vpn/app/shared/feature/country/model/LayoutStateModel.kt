package com.mooncloak.vpn.app.shared.feature.country.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.LoadState
import com.mooncloak.kodetools.pagex.ResolvedPage
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.api.shared.location.CountryFilters
import com.mooncloak.vpn.api.shared.location.CountryPage
import com.mooncloak.vpn.api.shared.location.RegionDetails
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.country_list_default_region_type
import com.mooncloak.vpn.app.shared.resource.country_list_header_label_with_count
import com.mooncloak.vpn.app.shared.resource.country_list_title
import com.mooncloak.vpn.app.shared.resource.server_list_header_label
import org.jetbrains.compose.resources.stringResource

@Immutable
@OptIn(ExperimentalPaginationAPI::class)
public sealed interface LayoutStateModel {

    public val items: List<Any>
    public val append: LoadState
    public val refresh: LoadState
    public val lastCursor: Cursor?
}

@Immutable
@OptIn(ExperimentalPaginationAPI::class)
public data class CountryListLayoutStateModel public constructor(
    override val append: LoadState = LoadState.Incomplete,
    override val refresh: LoadState = LoadState.Incomplete,
    public val countries: List<CountryDetails> = emptyList(),
    public val lastPage: CountryPage? = null,
    public val filters: CountryFilters? = null
) : LayoutStateModel {

    override val items: List<CountryDetails> = countries

    override val lastCursor: Cursor? = lastPage?.info?.lastCursor
}

@Immutable
@OptIn(ExperimentalPaginationAPI::class)
public data class RegionListLayoutStateModel public constructor(
    public val countryDetails: CountryDetails
) : LayoutStateModel {

    override val items: List<RegionDetails> = countryDetails.regions.distinctBy { it.region.code }
    override val append: LoadState = LoadState.Complete
    override val refresh: LoadState = LoadState.Complete
    override val lastCursor: Cursor? = null
}

@Immutable
@OptIn(ExperimentalPaginationAPI::class)
public data class ServerListLayoutStateModel public constructor(
    override val append: LoadState = LoadState.Incomplete,
    override val refresh: LoadState = LoadState.Incomplete,
    public val countryDetails: CountryDetails,
    public val regionDetails: RegionDetails,
    public val servers: List<Server> = emptyList(),
    public val lastPage: ResolvedPage<Server>? = null,
    public val filters: ServerFilters? = null
) : LayoutStateModel {

    override val items: List<Server> = servers
    override val lastCursor: Cursor? = lastPage?.info?.lastCursor
}

public val LayoutStateModel.canAppendMore: Boolean
    inline get() = !this.append.endOfPaginationReached && this.append !is LoadState.Loading

public val LayoutStateModel.isLoading: Boolean
    inline get() = this.refresh is LoadState.Loading || this.append is LoadState.Loading

public val LayoutStateModel.label: String
    @Composable
    inline get() = when (this) {
        is CountryListLayoutStateModel -> stringResource(
            Res.string.country_list_header_label_with_count,
            stringResource(Res.string.country_list_title),
            this.countries.size
        )

        is RegionListLayoutStateModel -> stringResource(
            Res.string.country_list_header_label_with_count,
            this.countryDetails.country.regionType
                ?: stringResource(Res.string.country_list_default_region_type),
            this.countryDetails.regions.size
        )

        is ServerListLayoutStateModel -> stringResource(
            Res.string.country_list_header_label_with_count,
            stringResource(Res.string.server_list_header_label),
            this.servers.size
        )
    }
