package com.mooncloak.vpn.api.shared.location

import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalPaginationAPI::class)
@Serializable
public data class CountryPage public constructor(
    @SerialName(value = "countries") public val countries: List<CountryDetails> = emptyList(),
    @SerialName(value = "info") public val info: CountryPageInfo = CountryPageInfo()
)

@OptIn(ExperimentalPaginationAPI::class)
@Serializable
public data class CountryPageInfo public constructor(
    @SerialName(value = "has_previous") public val hasPrevious: Boolean? = null,
    @SerialName(value = "has_next") public val hasNext: Boolean? = null,
    @SerialName(value = "first_cursor") public val firstCursor: Cursor? = null,
    @SerialName(value = "last_cursor") public val lastCursor: Cursor? = null,
    @SerialName(value = "total") public val totalCount: Int? = null
)
