package com.mooncloak.vpn.util.shared.crypto

public interface AesEncryptor : Encryptor<AesEncryptedData> {

    public companion object
}

public expect operator fun AesEncryptor.Companion.invoke(): AesEncryptor
