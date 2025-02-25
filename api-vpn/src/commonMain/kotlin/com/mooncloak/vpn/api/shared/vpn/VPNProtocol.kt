package com.mooncloak.vpn.api.shared.vpn

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

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
