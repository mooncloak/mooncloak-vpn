package com.mooncloak.vpn.app.shared.feature.home.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface HomeBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "empty")
    data object Empty : HomeBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "payment")
    data object Payment : HomeBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "server_details")
    data object ServerDetails : HomeBottomSheetDestination
}
