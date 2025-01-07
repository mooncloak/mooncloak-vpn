package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.TunnelStats
import com.wireguard.android.backend.Statistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a WireGuard Tunnel VPN connection. This class implements the [com.wireguard.android.backend.Tunnel]
 * interface and provides some useful functionality for accessing the current state and values of this
 * [com.wireguard.android.backend.Tunnel]. Use the [WireGuardTunnelManager] to obtain instances of this class.
 */
@OptIn(ExperimentalUuidApi::class)
public class WireGuardTunnel internal constructor(
    override val tunnelName: String,
    override val sessionId: String? = Uuid.random().toHexString(),
    override val server: Server? = null
) : com.wireguard.android.backend.Tunnel,
    com.mooncloak.vpn.app.shared.api.vpn.Tunnel {

    override val stats: TunnelStats? = null

    public val state: StateFlow<com.wireguard.android.backend.Tunnel.State>
        get() = mutableState.asStateFlow()

    public val statistics: StateFlow<Statistics?>
        get() = mutableStatistics.asStateFlow()

    override fun getName(): String = tunnelName

    private val mutableState = MutableStateFlow(com.wireguard.android.backend.Tunnel.State.DOWN)
    private val mutableStatistics = MutableStateFlow<Statistics?>(null)

    override fun onStateChange(newState: com.wireguard.android.backend.Tunnel.State) {
        mutableState.value = newState
    }

    internal fun onStatisticsChanged(statistics: Statistics?) {
        mutableStatistics.value = statistics
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WireGuardTunnel) return false

        // The WireGuard library only uses the name to identify tunnels. So, if the name is the same, we consider it
        // the same tunnel instance.
        return tunnelName == other.tunnelName
    }

    override fun hashCode(): Int = tunnelName.hashCode()

    override fun toString(): String =
        "WireGuardTunnel(name='$tunnelName', state=$state, statistics=$statistics)"
}
