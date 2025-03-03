package com.mooncloak.vpn.app.shared.feature.country.model

import androidx.compose.runtime.Immutable
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.locale.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface CountryListBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "servers")
    data class ServerList internal constructor(
        val country: Country,
        val region: Region
    ) : CountryListBottomSheetDestination
}
