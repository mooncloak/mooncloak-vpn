package com.mooncloak.vpn.app.shared.api.network

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.Region
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class LocalNetworkInfo public constructor(
    @SerialName(value = "ip") public val ipAddress: String? = null,
    @SerialName(value = "country") public val country: Country? = null,
    @SerialName(value = "region") public val region: Region? = null
)
