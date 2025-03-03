package com.mooncloak.vpn.api.shared.location

import com.mooncloak.kodetools.locale.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents details about a particular [Region] for the VPN service.
 *
 * @property [region] The [Region] that this model represents details about.
 *
 * @property [serverCount] The amount of available within this country.
 */
@Serializable
public data class RegionDetails public constructor(
    @SerialName(value = "region") public val region: Region,
    @SerialName(value = "server_count") public val serverCount: Int? = null,
)
