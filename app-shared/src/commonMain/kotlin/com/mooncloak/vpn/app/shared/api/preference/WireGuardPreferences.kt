package com.mooncloak.vpn.app.shared.api.preference

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class WireGuardPreferences public constructor(
    @SerialName(value = "dns_address") public val dnsAddresses: Set<String> = setOf(
        "1.1.1.1",
        "8.8.8.8"
    ),
    @SerialName(value = "allowed_ip") public val allowedIp: String = "0.0.0.0/0"
)
