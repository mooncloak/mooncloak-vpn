package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.vpn.app.shared.api.key.Base64Key
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyPair
import com.wireguard.crypto.KeyPair

internal class AndroidWireGuardConnectionKeyPair internal constructor(
    internal val keyPair: KeyPair
) : WireGuardConnectionKeyPair {

    override val publicKey: Base64Key = Base64Key(value = keyPair.publicKey.toBase64())

    override val privateKey: Base64Key = Base64Key(value = keyPair.privateKey.toBase64())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AndroidWireGuardConnectionKeyPair) return false

        if (publicKey != other.publicKey) return false

        return privateKey == other.privateKey
    }

    override fun hashCode(): Int {
        var result = publicKey.hashCode()
        result = 31 * result + privateKey.hashCode()
        return result
    }

    override fun toString(): String = "AndroidWireGuardConnectionKeyPair(publicKey=$publicKey, privateKey=REDACTED)"
}

internal fun KeyPair.toWireGuardConnectionKeyPair(): AndroidWireGuardConnectionKeyPair =
    AndroidWireGuardConnectionKeyPair(keyPair = this)
