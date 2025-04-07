package com.mooncloak.vpn.util.shared.crypto

public interface EncryptedData {

    public val algorithm: String
    public val value: ByteArray

    public companion object
}
