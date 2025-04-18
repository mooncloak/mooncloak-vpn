package com.mooncloak.vpn.component.stargate.entanglement

public fun interface DnsTxtRecordResolver {

    public suspend fun resolve(domain: String): List<String>

    public companion object
}
