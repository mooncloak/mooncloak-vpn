package com.mooncloak.vpn.app.shared.api.location

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a region within a country, such as a US State.
 *
 * @property [name] The name of this region.
 *
 * @property [flag] The image URI [String] of the flag for this region.
 *
 * @property [serverCount] The amount of servers available within this region.
 */
@Immutable
@Serializable
public data class Region public constructor(
    @SerialName(value = "code") public val code: RegionCode,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "flag") public val flag: String? = null,
    @SerialName(value = "server_count") public val serverCount: Int? = null
)
