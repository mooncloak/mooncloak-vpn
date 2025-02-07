package com.mooncloak.vpn.app.shared.api.location

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Region public constructor(
    @SerialName(value = "code") public val code: RegionCode,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "flag") public val flag: String? = null,
)
