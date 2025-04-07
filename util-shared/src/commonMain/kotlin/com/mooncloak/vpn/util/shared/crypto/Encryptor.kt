package com.mooncloak.vpn.util.shared.crypto

public interface Encryptor<Data : EncryptedData> {

    public suspend fun encrypt(value: ByteArray, keyMaterial: ByteArray): Data

    public suspend fun decrypt(data: Data, keyMaterial: ByteArray): ByteArray

    public companion object
}

/**
 * Encrypts the given value using a string-based key material, converting it to UTF-8 bytes.
 */
public suspend fun <Data : EncryptedData> Encryptor<Data>.encrypt(value: ByteArray, keyMaterial: String): Data =
    encrypt(value, keyMaterial.encodeToByteArray())

/**
 * Encrypts the given value using a string-based key material, converting it to UTF-8 bytes.
 */
public suspend fun <Data : EncryptedData> Encryptor<Data>.encrypt(value: String, keyMaterial: String): Data =
    encrypt(value.encodeToByteArray(), keyMaterial.encodeToByteArray())

/**
 * Encrypts the given value using a string-based key material, converting it to UTF-8 bytes.
 */
public suspend fun <Data : EncryptedData> Encryptor<Data>.encrypt(value: String, keyMaterial: ByteArray): Data =
    encrypt(value.encodeToByteArray(), keyMaterial)

/**
 * Decrypts the given data using a string-based key material, converting it to UTF-8 bytes.
 */
public suspend fun <Data : EncryptedData> Encryptor<Data>.decrypt(data: Data, keyMaterial: String): ByteArray =
    decrypt(data, keyMaterial.encodeToByteArray())
