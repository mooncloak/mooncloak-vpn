package com.mooncloak.vpn.app.shared.feature.server.list.model

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.Server
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface ServerListBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "payment")
    data object Payment : ServerListBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "server_details")
    data class ServerDetails internal constructor(
        val server: Server
    ) : ServerListBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "connection")
    data object ServerConnection : ServerListBottomSheetDestination
}
