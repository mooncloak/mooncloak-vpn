package com.mooncloak.vpn.api.shared.network.ping

public actual val PingProtocol.Companion.PlatformDefault: PingProtocol
    get() = PingProtocol.ICMP
