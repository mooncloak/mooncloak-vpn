package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.TunnelStats
import com.mooncloak.vpn.app.shared.api.vpn.Tunnel
import com.wireguard.android.backend.Statistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Represents a WireGuard Tunnel VPN connection. This class implements the [com.wireguard.android.backend.Tunnel]
 * interface and provides some useful functionality for accessing the current state and values of this
 * [com.wireguard.android.backend.Tunnel]. Use the [WireGuardTunnelManager] to obtain instances of this class.
 */
public class WireGuardTunnel internal constructor(
    override val name: String,
    override val sessionId: String?,
    override val server: Server?
) : com.wireguard.android.backend.Tunnel,
    Tunnel {

    override val stats: TunnelStats? = null

    public val state: StateFlow<com.wireguard.android.backend.Tunnel.State>
        get() = mutableState.asStateFlow()

    public val statistics: StateFlow<Statistics?>
        get() = mutableStatistics.asStateFlow()

    override fun getName(): String = name

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

        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String =
        "WireGuardTunnel(name='$name', state=$state, statistics=$statistics)"
}
