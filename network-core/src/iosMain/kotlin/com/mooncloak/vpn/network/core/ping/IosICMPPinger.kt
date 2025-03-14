package com.mooncloak.vpn.network.core.ping

import kotlin.time.Duration

internal data object IosICMPPinger : Pinger {

    override val protocol: PingProtocol = PingProtocol.ICMP

    // FIXME: Might not work on iOS if phone not jail broken. Use another method like TCP.
    override suspend fun ping(host: String, port: Int?, timeout: Duration): Duration? = null
}
