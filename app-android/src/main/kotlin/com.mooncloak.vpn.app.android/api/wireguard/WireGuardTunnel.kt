package com.mooncloak.vpn.app.android.api.wireguard

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.vpn.TunnelStats
import com.wireguard.android.backend.Statistics
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a WireGuard Tunnel VPN connection. This class implements the [com.wireguard.android.backend.Tunnel]
 * interface and provides some useful functionality for accessing the current state and values of this
 * [com.wireguard.android.backend.Tunnel]. Use the [WireGuardTunnelManager] to obtain instances of this class.
 */
@Stable
@OptIn(ExperimentalUuidApi::class)
public class WireGuardTunnel internal constructor(
    override val tunnelName: String,
    override val sessionId: String? = Uuid.random().toHexString(),
    override val server: Server? = null
) : com.wireguard.android.backend.Tunnel,
    com.mooncloak.vpn.app.shared.api.vpn.Tunnel {

    private val mutableState = mutableStateOf(com.wireguard.android.backend.Tunnel.State.DOWN)
    private val mutableStats = mutableStateOf<TunnelStats?>(null)

    override val stats: TunnelStats? by mutableStats

    public val state: com.wireguard.android.backend.Tunnel.State by mutableState

    override fun getName(): String = tunnelName

    override fun onStateChange(newState: com.wireguard.android.backend.Tunnel.State) {
        mutableState.value = newState
    }

    internal fun onStatisticsChanged(statistics: Statistics?) {
        mutableStats.value = statistics?.let { stats ->
            // TODO: How should I handle this? There is no documentation in the WireGuard library.
            val peerStats = stats.peers().map { key -> stats.peer(key) }
                .firstOrNull()

            TunnelStats(
                totalRx = stats.totalRx(),
                totalTx = stats.totalTx(),
                rxThroughput = peerStats?.rxBytes,
                txThroughput = peerStats?.txBytes
            )
        }
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
        "WireGuardTunnel(name='$tunnelName', state=$state, stats=$stats)"
}
