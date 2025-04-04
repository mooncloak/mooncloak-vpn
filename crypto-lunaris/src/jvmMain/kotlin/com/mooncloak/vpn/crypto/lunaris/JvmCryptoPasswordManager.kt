package com.mooncloak.vpn.crypto.lunaris

import java.io.File
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public operator fun CryptoPasswordManager.Companion.invoke(): CryptoPasswordManager =
    JvmCryptoPasswordManager()

@OptIn(ExperimentalEncodingApi::class)
internal class JvmCryptoPasswordManager internal constructor() : CryptoPasswordManager {

    private val storageDir: File = CryptoWalletManager.walletDirectory()
    private val passwordFile = File(storageDir, "wallet_password.enc")
    private val keyFile = File(storageDir, "encryption_key.bin")
    private val ivLength = 12 // GCM IV length in bytes
    private val tagLength = 128 // GCM authentication tag length in bits

    // Load or generate the encryption key
    private val encryptionKey: SecretKey
        get() = if (keyFile.exists()) {
            val keyBytes = keyFile.readBytes()
            SecretKeySpec(keyBytes, "AES")
        } else {
            val keyGen = KeyGenerator.getInstance("AES")
            keyGen.init(256) // 256-bit AES key
            val key = keyGen.generateKey()
            keyFile.writeBytes(key.encoded)
            keyFile.setReadable(true, true) // Restrict to owner
            keyFile.setWritable(true, true)
            key
        }

    override suspend fun current(): String? = withContext(Dispatchers.IO) {
        try {
            if (!passwordFile.exists()) return@withContext null

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val encryptedBytes = passwordFile.readBytes()
            val iv = encryptedBytes.take(ivLength).toByteArray()
            val ciphertext = encryptedBytes.drop(ivLength).toByteArray()

            val gcmSpec = GCMParameterSpec(tagLength, iv)

            cipher.init(Cipher.DECRYPT_MODE, encryptionKey, gcmSpec)

            val decryptedBytes = cipher.doFinal(ciphertext)

            String(decryptedBytes)
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

        // Encrypt and store it
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = ByteArray(ivLength).apply { random.nextBytes(this) }
        val gcmSpec = GCMParameterSpec(tagLength, iv)
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, gcmSpec)
        val encryptedBytes = cipher.doFinal(password.toByteArray())

        // Write IV + ciphertext to file
        passwordFile.writeBytes(iv + encryptedBytes)
        passwordFile.setReadable(true, true) // Restrict to owner
        passwordFile.setWritable(true, true)

        password
    }
}
