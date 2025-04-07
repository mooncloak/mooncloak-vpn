package com.mooncloak.vpn.util.shared.crypto

public interface Encryptor<Data : EncryptedData> {

    public suspend fun encrypt(value: ByteArray, keyMaterial: ByteArray): Data

    public suspend fun decrypt(data: Data, keyMaterial: ByteArray): ByteArray

    public companion object
}
