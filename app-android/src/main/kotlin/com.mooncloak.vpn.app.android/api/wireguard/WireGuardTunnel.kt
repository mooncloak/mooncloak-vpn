package com.mooncloak.vpn.app.android.api.wireguard

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.mooncloak.vpn.api.shared.tunnel.Tunnel
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import com.mooncloak.vpn.api.shared.tunnel.TunnelStats
import com.wireguard.android.backend.Statistics
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Represents a WireGuard Tunnel VPN connection. This class implements the [com.wireguard.android.backend.Tunnel]
 * interface and provides some useful functionality for accessing the current state and values of this
 * [com.wireguard.android.backend.Tunnel]. Use the [AndroidWireGuardTunnelManager] to obtain instances of this class.
 */
@Stable
@OptIn(ExperimentalUuidApi::class)
public class WireGuardTunnel internal constructor(
    override val tunnelName: String,
    override val sessionId: String? = Uuid.random().toHexString(),
    override val server: Server? = null,
    initialStats: TunnelStats? = null,
    initialStatus: VPNConnectionStatus = VPNConnectionStatus.Disconnected
) : com.wireguard.android.backend.Tunnel,
    Tunnel {

    private val mutableStats = mutableStateOf(initialStats)
    private val mutableStatus = mutableStateOf(initialStatus)

    override val stats: State<TunnelStats?>
        get() = mutableStats

    override val status: State<VPNConnectionStatus>
        get() = mutableStatus

    override fun getName(): String = tunnelName

    override fun onStateChange(newState: com.wireguard.android.backend.Tunnel.State) {
        mutableStatus.value = when (newState) {
            com.wireguard.android.backend.Tunnel.State.DOWN -> VPNConnectionStatus.Disconnected
            com.wireguard.android.backend.Tunnel.State.TOGGLE -> VPNConnectionStatus.Connecting
            com.wireguard.android.backend.Tunnel.State.UP -> VPNConnectionStatus.Connected
        }
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
        "WireGuardTunnel(stats=$stats, status=$status, server=$server, sessionId=$sessionId, tunnelName='$tunnelName')"
}
