package com.mooncloak.vpn.network.core.tunnel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.VPNConnectionStatus
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
public data class IosTunnel public constructor(
    override val tunnelName: String,
    override val sessionId: String = Uuid.random().toHexString(),
    override val server: Server? = null,
) : Tunnel {

    public constructor(
        server: Server,
        sessionId: String = Uuid.random().toHexString()
    ) : this(
        tunnelName = server.name,
        server = server,
        sessionId = sessionId
    )

    override val stats: State<TunnelStats?>
        get() = mutableStats

    override val status: State<VPNConnectionStatus>
        get() = mutableStatus

    private val mutableStats = mutableStateOf<TunnelStats?>(null)
    private val mutableStatus = mutableStateOf(VPNConnectionStatus.Disconnected)

    public fun updateStats(
        rxThroughput: Long? = null,
        txThroughput: Long? = null,
        totalRx: Long? = null,
        totalTx: Long? = null
    ) {
        mutableStats.value = TunnelStats(
            rxThroughput = rxThroughput,
            txThroughput = txThroughput,
            totalRx = totalRx,
            totalTx = totalTx
        )
    }

    public fun updateStatus(status: VPNConnectionStatus) {
        mutableStatus.value = status
    }
}
