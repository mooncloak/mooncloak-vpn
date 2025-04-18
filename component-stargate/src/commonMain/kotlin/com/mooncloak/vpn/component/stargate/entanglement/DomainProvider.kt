package com.mooncloak.vpn.component.stargate.entanglement

public fun interface DomainProvider {

    public suspend fun get(): String

    public companion object
}
