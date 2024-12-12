package com.mooncloak.vpn.app.shared.feature.settings.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface SettingsBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "empty")
    data object Empty : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "dependencies")
    data object DependencyLicenseList : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "subscription")
    data object Subscription : SettingsBottomSheetDestination
}
