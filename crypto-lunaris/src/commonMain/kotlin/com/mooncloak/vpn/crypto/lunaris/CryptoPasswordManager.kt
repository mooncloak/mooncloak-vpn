package com.mooncloak.vpn.crypto.lunaris

public interface CryptoPasswordManager {

    public suspend fun current(): String?

    public suspend fun generate(): String

    public companion object
}

public suspend fun CryptoPasswordManager.getCurrentOrGenerate(): String =
    current() ?: generate()
