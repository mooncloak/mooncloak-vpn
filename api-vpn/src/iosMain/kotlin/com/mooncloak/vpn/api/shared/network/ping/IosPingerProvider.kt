package com.mooncloak.vpn.api.shared.network.ping

public actual operator fun PingerProvider.Companion.invoke(): PingerProvider =
    IosPingerProvider()

internal class IosPingerProvider internal constructor() : PingerProvider {

    override fun get(protocol: PingProtocol): Pinger? =
        when (protocol) {
            PingProtocol.ICMP -> IosICMPPinger
            else -> null
        }
}
