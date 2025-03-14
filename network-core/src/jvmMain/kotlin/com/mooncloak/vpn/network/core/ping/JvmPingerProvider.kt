package com.mooncloak.vpn.network.core.ping

public actual operator fun PingerProvider.Companion.invoke(): PingerProvider =
    JvmPingerProvider()

internal class JvmPingerProvider internal constructor() : PingerProvider {

    override fun get(protocol: PingProtocol): Pinger? =
        when (protocol) {
            PingProtocol.ICMP -> JvmICMPPinger
            PingProtocol.TCP -> JvmTCPPinger
            else -> null
        }
}
