package com.mooncloak.vpn.network.core.ping

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.time.Duration
import kotlin.time.measureTime

internal data object AndroidTCPPinger : Pinger {

    override val protocol: PingProtocol = PingProtocol.TCP

    override suspend fun ping(host: String, port: Int?, timeout: Duration): Duration? =
        withContext(Dispatchers.IO) {
            try {
                val timeoutMilliseconds = timeout.inWholeMilliseconds.toInt()

                return@withContext measureTime {
                    Socket().use { socket ->
                        socket.connect(InetSocketAddress(host, port ?: 80), timeoutMilliseconds)
                    }
                }
            } catch (_: Exception) {
                null
            }
        }
}
