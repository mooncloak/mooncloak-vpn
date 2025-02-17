package com.mooncloak.vpn.app.shared.feature.settings.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal sealed interface SettingsBottomSheetDestination {

    @Immutable
    @Serializable
    @SerialName(value = "dependencies")
    data object DependencyLicenseList : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "subscription")
    data object Subscription : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "transaction_history")
    data object TransactionHistory : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "plan")
    data object SelectPlan : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "app_info")
    data class AppInfo internal constructor(
        val details: SettingsAppDetails
    ) : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "device_info")
    data class DeviceInfo internal constructor(
        val details: SettingsDeviceDetails
    ) : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "collaborators")
    data object Collaborators : SettingsBottomSheetDestination
}
