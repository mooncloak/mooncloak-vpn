package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@JvmInline
public value class VPNProtocol public constructor(
    public val value: String
) {

    public companion object {

        public val WireGuard: VPNProtocol = VPNProtocol(value = "wireguard")
        public val OpenVPN: VPNProtocol = VPNProtocol(value = "openvpn")
    }
}
