package com.mooncloak.vpn.util.shared.crypto

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

public actual operator fun AesEncryptor.Companion.invoke(): AesEncryptor =
    JvmAesEncryptor()

// Uses AES-256 (256-bit key from PBKDF2) with CBC mode and PKCS5 padding (equivalent to PKCS7).
internal class JvmAesEncryptor internal constructor() : AesEncryptor {

    override suspend fun encrypt(value: ByteArray, keyMaterial: ByteArray): AesEncryptedData {
        val iv = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val salt = ByteArray(16).apply { SecureRandom().nextBytes(this) }
        val key = deriveKey(keyMaterial, salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding") // PKCS5 = PKCS7 for AES

        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))

        val encryptedBytes = cipher.doFinal(value)

        return AesEncryptedData(
            value = encryptedBytes,
            iv = iv,
            salt = salt
        )
    }

    override suspend fun decrypt(data: AesEncryptedData, keyMaterial: ByteArray): ByteArray {
        val key = deriveKey(keyMaterial, data.salt)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(data.iv))

        return cipher.doFinal(data.value)
    }

    private fun deriveKey(keyMaterial: ByteArray, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(keyMaterial.decodeToString().toCharArray(), salt, 10000, 256) // 256-bit key
        val tmp = factory.generateSecret(spec)

        return SecretKeySpec(tmp.encoded, "AES")
    }
}
