package com.mooncloak.vpn.util.shared.crypto

public interface AesEncryptor : Encryptor<AesEncryptedData> {

    public companion object
}

public expect operator fun AesEncryptor.Companion.invoke(): AesEncryptor

/**
 * Encrypts the given value using a string-based key material, converting it to UTF-8 bytes.
 */
public suspend fun AesEncryptor.encrypt(value: ByteArray, keyMaterial: String): AesEncryptedData =
    encrypt(value, keyMaterial.encodeToByteArray())

/**
 * Decrypts the given data using a string-based key material, converting it to UTF-8 bytes.
 */
public suspend fun AesEncryptor.decrypt(data: AesEncryptedData, keyMaterial: String): ByteArray =
    decrypt(data, keyMaterial.encodeToByteArray())
