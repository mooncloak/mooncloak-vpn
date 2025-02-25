package com.mooncloak.vpn.app.shared.feature.home.model

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.api.shared.server.Server
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface HomeBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "payment")
    data object Payment : HomeBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "server_details")
    data class ServerDetails internal constructor(
        val server: Server
    ) : HomeBottomSheetDestination
}
