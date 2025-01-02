package com.mooncloak.vpn.app.android.api.wireguard

import com.wireguard.android.backend.Statistics
import com.wireguard.android.backend.Tunnel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Represents a WireGuard Tunnel VPN connection. This class implements the [Tunnel] interface and provides some useful
 * functionality for accessing the current state and values of this [Tunnel]. Use the [WireGuardTunnelManager] to
 * obtain instances of this class.
 */
public class WireGuardTunnel internal constructor(
    private val name: String
) : Tunnel {

    public val state: StateFlow<Tunnel.State>
        get() = mutableState.asStateFlow()

    public val statistics: StateFlow<Statistics?>
        get() = mutableStatistics.asStateFlow()

    override fun getName(): String = name

    private val mutableState = MutableStateFlow(Tunnel.State.DOWN)
    private val mutableStatistics = MutableStateFlow<Statistics?>(null)

    override fun onStateChange(newState: Tunnel.State) {
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
