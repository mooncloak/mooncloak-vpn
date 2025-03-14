package com.mooncloak.vpn.network.core.ping

public actual val PingProtocol.Companion.PlatformDefault: PingProtocol
    get() = PingProtocol.ICMP
