package com.mooncloak.vpn.app.shared.feature.main.model

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.server.Server
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface MainBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "connection")
    data class ServerConnection internal constructor(
        val server: Server? = null
    ) : MainBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "plan")
    data object SelectPlan : MainBottomSheetDestination
}
