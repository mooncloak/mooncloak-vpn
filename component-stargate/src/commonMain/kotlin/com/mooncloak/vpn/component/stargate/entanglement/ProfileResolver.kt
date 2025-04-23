package com.mooncloak.vpn.component.stargate.entanglement

public interface ProfileResolver {

    public suspend fun resolve(document: DIDDocument): Profile?

    public companion object
}
