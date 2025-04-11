package com.mooncloak.vpn.app.shared.api.wireguard

import com.mooncloak.vpn.api.shared.key.Base64Key
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyPair

public operator fun WireGuardConnectionKeyPair.Companion.invoke(
    publicKey: Base64Key,
    privateKey: Base64Key
): WireGuardConnectionKeyPair = IosWireGuardConnectionKeyPair(
    publicKey = publicKey,
    privateKey = privateKey
)

public operator fun WireGuardConnectionKeyPair.Companion.invoke(
    publicKey: String,
    privateKey: String
): WireGuardConnectionKeyPair = IosWireGuardConnectionKeyPair(
    publicKey = publicKey,
    privateKey = privateKey
)

public class IosWireGuardConnectionKeyPair public constructor(
    override val publicKey: Base64Key,
    override val privateKey: Base64Key
) : WireGuardConnectionKeyPair {

    public constructor(
        publicKey: String,
        privateKey: String
    ) : this(
        publicKey = Base64Key(value = publicKey),
        privateKey = Base64Key(value = privateKey)
    )

    public val publicKeyBase64: String
        get() = publicKey.value

    public val privateKeyBase64: String
        get() = privateKey.value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IosWireGuardConnectionKeyPair) return false

        if (publicKey != other.publicKey) return false

        return privateKey == other.privateKey
    }

    override fun hashCode(): Int {
        var result = publicKey.hashCode()
        result = 31 * result + privateKey.hashCode()
        return result
    }

    override fun toString(): String {
        return "IosWireGuardConnectionKeyPair(publicKey=$publicKey, privateKey=REDACTED)"
    }
}
