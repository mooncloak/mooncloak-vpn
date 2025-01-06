package com.mooncloak.vpn.app.shared.api.key

public interface WireGuardConnectionKeyPair {

    public val publicKey: Base64Key
    public val privateKey: Base64Key

    public companion object
}
