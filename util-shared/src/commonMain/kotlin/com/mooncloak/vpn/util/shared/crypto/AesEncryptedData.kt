package com.mooncloak.vpn.util.shared.crypto

public data class AesEncryptedData public constructor(
    override val value: ByteArray,
    public val iv: ByteArray,
    public val salt: ByteArray
) : EncryptedData {

    override val algorithm: String = "AES/CBC/PKCS7Padding"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AesEncryptedData) return false

        return value.contentEquals(other.value) &&
                iv.contentEquals(other.iv) &&
                salt.contentEquals(other.salt)
    }

    override fun hashCode(): Int {
        var result = value.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}
