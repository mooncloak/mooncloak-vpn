package com.mooncloak.vpn.api.shared.server

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Immutable
@Serializable
@JvmInline
public value class ConnectionType public constructor(
    public val value: String
) {

    public companion object {

        public val MultipleVpn: ConnectionType = ConnectionType(value = "multiple") // "Secure Core" for ProtonVPN
        public val P2P: ConnectionType = ConnectionType(value = "p2p")
        public val Tor: ConnectionType = ConnectionType(value = "tor")
    }
}
