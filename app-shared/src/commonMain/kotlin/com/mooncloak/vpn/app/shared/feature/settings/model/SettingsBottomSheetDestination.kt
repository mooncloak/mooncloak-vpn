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
    @SerialName(value = "device_info")
    data class DeviceInfo internal constructor(
        val details: SettingsDeviceDetails
    ) : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "collaborators")
    data object Collaborators : SettingsBottomSheetDestination

    @Immutable
    @Serializable
    @SerialName(value = "dns_servers")
    data object DnsServerConfig : SettingsBottomSheetDestination
}
