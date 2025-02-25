package com.mooncloak.vpn.api.shared.reflection

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class HttpReflection public constructor(
    @SerialName(value = "ip") public val ipAddress: String? = null
)
