package com.mooncloak.vpn.app.shared.api.location

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed interface Region {

    public val code: RegionCode
    public val name: String
    public val flag: String?
}

@Immutable
@Serializable
@SerialName(value = "default")
public data class DefaultRegion public constructor(
    @SerialName(value = "code") public override val code: RegionCode,
    @SerialName(value = "name") public override val name: String,
    @SerialName(value = "flag") public override val flag: String? = null,
) : Region

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
@SerialName(value = "serviceable")
public data class RegionWithVPNService public constructor(
    @SerialName(value = "code") public override val code: RegionCode,
    @SerialName(value = "name") public override val name: String,
    @SerialName(value = "flag") public override val flag: String? = null,
    @SerialName(value = "server_count") public val serverCount: Int? = null
) : Region

public operator fun Region.Companion.invoke(
    code: RegionCode,
    name: String,
    flag: String? = null
): Region = DefaultRegion(
    code = code,
    name = name,
    flag = flag
)

public fun Region.toServiceable(
    serverCount: Int? = null
): RegionWithVPNService = if (this is RegionWithVPNService) {
    this.copy(serverCount = serverCount)
} else {
    RegionWithVPNService(
        code = this.code,
        name = this.name,
        flag = this.flag,
        serverCount = serverCount
    )
}
