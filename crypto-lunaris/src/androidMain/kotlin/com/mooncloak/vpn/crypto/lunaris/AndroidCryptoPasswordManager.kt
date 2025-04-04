package com.mooncloak.vpn.crypto.lunaris

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File
import java.security.SecureRandom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

public operator fun CryptoPasswordManager.Companion.invoke(context: Context): CryptoPasswordManager =
    AndroidCryptoPasswordManager(context = context)

@OptIn(ExperimentalEncodingApi::class)
internal class AndroidCryptoPasswordManager internal constructor(
    private val context: Context
) : CryptoPasswordManager {

    private val passwordFile = File(CryptoWalletManager.walletDirectory(context), "wallet_password.enc")
    private val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    override suspend fun current(): String? = withContext(Dispatchers.IO) {
        try {
            if (!passwordFile.exists()) return@withContext null

            val encryptedFile = EncryptedFile.Builder(
                passwordFile,
                context,
                masterKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()

            encryptedFile.openFileInput().use { input ->
                String(input.readBytes())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun generate(): String = withContext(Dispatchers.IO) {
        // Generate a strong random password (32 bytes, 256-bit entropy)
        val random = SecureRandom()
        val passwordBytes = ByteArray(32)
        random.nextBytes(passwordBytes)
        val password = Base64.UrlSafe.encode(passwordBytes)

        // Store it encrypted with MasterKey
        val encryptedFile = EncryptedFile.Builder(
            passwordFile,
            context,
            masterKey,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        encryptedFile.openFileOutput().use { output ->
            output.write(password.toByteArray())
        }

        password
    }
}
