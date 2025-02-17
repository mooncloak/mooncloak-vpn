package com.mooncloak.vpn.app.shared.feature.settings.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class SettingsDeviceDetails public constructor(
    @SerialName(value = "public_ip") public val publicIpAddress: String? = null,
    @SerialName(value = "local_ip") public val localIpAddress: String? = null
)
