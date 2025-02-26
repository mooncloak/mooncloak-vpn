package com.mooncloak.vpn.api.shared.network.ping

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import kotlin.time.Duration
import kotlin.time.measureTime

internal data object AndroidICMPPinger : Pinger {

    override val protocol: PingProtocol = PingProtocol.ICMP

    override suspend fun ping(
        host: String,
        port: Int?,
        timeout: Duration
    ): Duration? = withContext(Dispatchers.IO) {
        val address = InetAddress.getByName(host)
        val timeoutMilliseconds = timeout.inWholeMilliseconds.toInt()

        return@withContext measureTime {
            val reachable = address.isReachable(timeoutMilliseconds)
            if (!reachable) return@withContext null
        }
    }
}
