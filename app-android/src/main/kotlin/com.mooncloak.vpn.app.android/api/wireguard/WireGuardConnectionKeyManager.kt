package com.mooncloak.vpn.app.android.api.wireguard

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.key.KeyManager
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.wireguard.crypto.Key
import com.wireguard.crypto.KeyPair
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class WireGuardConnectionKeyManager @Inject internal constructor(
    context: ApplicationContext
) : KeyManager<KeyPair> {

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        FILE_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val mutex = Mutex(locked = false)

    override suspend fun generate(): KeyPair =
        KeyPair()

    override suspend fun get(): KeyPair? {
        val privateKey = sharedPreferences.getString(KEY_PRIVATE_KEY, null) ?: return null

        return KeyPair(Key.fromBase64(privateKey))
    }

    override suspend fun store(material: KeyPair) {
        mutex.withLock {
            sharedPreferences.edit()
                .putString(KEY_PRIVATE_KEY, material.privateKey.toBase64())
                .apply()
        }
    }

    internal companion object {

        internal const val FILE_NAME: String = "wire_guard_keys"
        internal const val KEY_PRIVATE_KEY: String = "com.mooncloak.vpn.app.android.key.wireguard.private"
    }
}
