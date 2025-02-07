package com.mooncloak.vpn.app.shared.api.location

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Country public constructor(
    @SerialName(value = "code") public val code: CountryCode,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "flag") public val flag: String? = null,
    @SerialName(value = "regions") public val regions: List<Region> = emptyList(),
    @SerialName(value = "region_type") public val regionType: String? = null
)
