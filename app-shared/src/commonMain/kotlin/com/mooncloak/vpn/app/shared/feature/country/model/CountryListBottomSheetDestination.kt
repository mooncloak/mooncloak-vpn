package com.mooncloak.vpn.app.shared.feature.country.model

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface CountryListBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "regions")
    data class RegionList internal constructor(
        val country: Country
    ) : CountryListBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "servers")
    data class ServerList internal constructor(
        val country: Country,
        val region: Region
    ) : CountryListBottomSheetDestination
}
