package com.mooncloak.vpn.crypto.lunaris

public operator fun CryptoPasswordManager.Companion.invoke(): CryptoPasswordManager = IosCryptoPasswordManager()

internal class IosCryptoPasswordManager internal constructor() : CryptoPasswordManager {

    override suspend fun current(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun generate(): String {
        TODO("Not yet implemented")
    }
}
