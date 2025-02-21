package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.runtime.Immutable
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.pagex.LoadState
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerFilters

@Immutable
public data class CountryListStateModel public constructor(
    public val connectedServer: Server? = null,
    public val countries: List<Country> = emptyList(),
    public val filters: ServerFilters? = null,
    public val prepend: LoadState = LoadState.Complete,
    public val append: LoadState = LoadState.Complete,
    public val refresh: LoadState = LoadState.Incomplete,
    public val errorMessage: String? = null
)

public val CountryListStateModel.canAppendMore: Boolean
    inline get() = !this.append.endOfPaginationReached && this.append !is LoadState.Loading

public val CountryListStateModel.isLoading: Boolean
    inline get() = this.refresh is LoadState.Loading || this.prepend is LoadState.Loading || this.append is LoadState.Loading
