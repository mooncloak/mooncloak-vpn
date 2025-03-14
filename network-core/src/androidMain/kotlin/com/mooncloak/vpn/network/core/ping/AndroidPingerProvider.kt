package com.mooncloak.vpn.network.core.ping

public actual operator fun PingerProvider.Companion.invoke(): PingerProvider =
    AndroidPingerProvider()

internal class AndroidPingerProvider internal constructor() : PingerProvider {

    override fun get(protocol: PingProtocol): Pinger? =
        when (protocol) {
            PingProtocol.ICMP -> AndroidICMPPinger
            PingProtocol.TCP -> AndroidTCPPinger
            else -> null
        }
}
