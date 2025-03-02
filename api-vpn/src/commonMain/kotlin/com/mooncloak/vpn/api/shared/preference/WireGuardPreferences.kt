package com.mooncloak.vpn.api.shared.preference

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class WireGuardPreferences public constructor(
    @SerialName(value = "dns_address") public val dnsAddresses: Set<String> = Defaults.DnsServers,
    @SerialName(value = "allowed_ips") public val allowedIps: Set<String> = Defaults.AllowedIps
) {

    @Suppress("ConstPropertyName")
    public object Defaults {

        public const val PrimaryDnsServer: String = "1.1.1.1"
        public const val SecondaryDnsServer: String = "8.8.8.8"
        public val DnsServers: Set<String> = setOf(
            PrimaryDnsServer,
            SecondaryDnsServer
        )

        public val AllowedIps: Set<String> = setOf("0.0.0.0/0", "::/0")
    }
}
