package com.mooncloak.vpn.api.shared.location

import com.mooncloak.vpn.api.shared.server.ConnectionType
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents filters that can be provided when querying [CountryDetails].
 *
 * @property [connectionTypes] A list of [ConnectionType] values that must be supported by the countries returned. A
 * value of `null` indicates no filter is applied. Defaults to `null`.
 *
 * @property [vpnProtocols] A list of [VPNProtocol] values that must be supported by the countries returned. A value
 * of `null` indicates no filter is applied. Defaults to `null`.
 */
@Serializable
public data class CountryFilters public constructor(
    @SerialName(value = Key.CONNECTION_TYPES) public val connectionTypes: Set<ConnectionType>? = null,
    @SerialName(value = Key.VPN_PROTOCOLS) public val vpnProtocols: Set<VPNProtocol>? = null
) {

    public object Key {

        public const val CONNECTION_TYPES: String = "types"
        public const val VPN_PROTOCOLS: String = "protocols"
    }
}
