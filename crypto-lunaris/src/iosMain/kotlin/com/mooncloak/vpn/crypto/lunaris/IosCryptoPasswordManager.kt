package com.mooncloak.vpn.crypto.lunaris

import com.mooncloak.vpn.crypto.lunaris.IosCryptoPasswordManager.Companion.SERVICE
import com.mooncloak.vpn.util.shared.crypto.Secure
import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

@OptIn(ExperimentalSettingsImplementation::class)
public operator fun CryptoPasswordManager.Companion.invoke(
    settings: Settings = KeychainSettings(service = SERVICE),
    secureRandom: Random = Random.Secure
): CryptoPasswordManager = IosCryptoPasswordManager(
    settings = settings,
    secureRandom = secureRandom
)

internal class IosCryptoPasswordManager internal constructor(
    private val settings: Settings,
    private val secureRandom: Random
) : CryptoPasswordManager {

    override suspend fun current(): String? =
        settings.getStringOrNull(KEY_PASSWORD)

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun generate(): String {
        val passwordBytes = ByteArray(32)
        secureRandom.nextBytes(passwordBytes)

        val password = Base64.UrlSafe.encode(passwordBytes)

        settings.putString(KEY_PASSWORD, password)

        return password
    }

    companion object {

        internal const val SERVICE: String = "com.mooncloak.vpn.app-ios.walletpassword"
        private const val KEY_PASSWORD: String = "wallet_password"
    }
}
