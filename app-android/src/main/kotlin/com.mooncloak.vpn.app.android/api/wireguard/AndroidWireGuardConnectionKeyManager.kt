package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.android.storage.EncryptedSharedPreferenceProvider
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyPair
import com.wireguard.crypto.Key
import com.wireguard.crypto.KeyPair
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import androidx.core.content.edit

internal class AndroidWireGuardConnectionKeyManager @Inject internal constructor(
    private val encryptedSharedPreferenceProvider: EncryptedSharedPreferenceProvider
) : WireGuardConnectionKeyManager {

    private val mutex = Mutex(locked = false)

    override suspend fun generate(): WireGuardConnectionKeyPair =
        KeyPair().toWireGuardConnectionKeyPair()

    override suspend fun get(): AndroidWireGuardConnectionKeyPair? {
        val sharedPreferences = encryptedSharedPreferenceProvider.get(FILE_NAME)
        val privateKey = sharedPreferences.getString(KEY_PRIVATE_KEY, null) ?: return null

        return KeyPair(Key.fromBase64(privateKey)).toWireGuardConnectionKeyPair()
    }

    override suspend fun store(material: WireGuardConnectionKeyPair) {
        mutex.withLock {
            val sharedPreferences = encryptedSharedPreferenceProvider.get(FILE_NAME)

            sharedPreferences.edit {
                putString(KEY_PRIVATE_KEY, material.privateKey.value)
            }
        }
    }

    internal companion object {

        internal const val FILE_NAME: String = "wire_guard_keys"
        internal const val KEY_PRIVATE_KEY: String = "com.mooncloak.vpn.app.android.key.wireguard.private"
    }
}
