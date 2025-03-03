package com.mooncloak.vpn.app.shared.feature.country

import androidx.compose.runtime.Immutable
import com.mooncloak.kodetools.pagex.LoadState
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.api.shared.location.CountryPage
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerFilters

@Immutable
public data class CountryListStateModel public constructor(
    public val connectedServer: Server? = null,
    public val countries: List<CountryDetails> = emptyList(),
    public val lastPage: CountryPage? = null,
    public val filters: ServerFilters? = null,
    public val append: LoadState = LoadState.Incomplete,
    public val refresh: LoadState = LoadState.Incomplete,
    public val errorMessage: String? = null
)

public val CountryListStateModel.canAppendMore: Boolean
    inline get() = !this.append.endOfPaginationReached && this.append !is LoadState.Loading

public val CountryListStateModel.isLoading: Boolean
    inline get() = this.refresh is LoadState.Loading || this.append is LoadState.Loading
