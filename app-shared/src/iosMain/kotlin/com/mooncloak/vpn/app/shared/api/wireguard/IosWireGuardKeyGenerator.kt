package com.mooncloak.vpn.app.shared.api.wireguard

public interface IosWireGuardKeyGenerator {

    public fun generatePrivateKey(): String

    public fun publicKeyFromPrivateKey(privateKey: String): String?

    public companion object
}
