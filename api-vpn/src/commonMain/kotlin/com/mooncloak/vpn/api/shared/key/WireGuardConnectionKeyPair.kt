package com.mooncloak.vpn.api.shared.key

public interface WireGuardConnectionKeyPair {

    public val publicKey: Base64Key
    public val privateKey: Base64Key

    public companion object
}
