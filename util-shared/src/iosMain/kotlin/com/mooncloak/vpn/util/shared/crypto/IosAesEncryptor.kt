package com.mooncloak.vpn.util.shared.crypto

import platform.CoreCrypto.*
import kotlinx.cinterop.*
import org.kotlincrypto.random.CryptoRand

public actual operator fun AesEncryptor.Companion.invoke(): AesEncryptor = IosAesEncryptor()

@OptIn(ExperimentalForeignApi::class)
internal class IosAesEncryptor internal constructor() : AesEncryptor {

    override suspend fun encrypt(value: ByteArray, keyMaterial: ByteArray): AesEncryptedData {
        val iv = ByteArray(16).apply { CryptoRand.Default.nextBytes(this) }
        val salt = ByteArray(16).apply { CryptoRand.Default.nextBytes(this) }
        val key = deriveKey(keyMaterial, salt)

        memScoped {
            val dataOut = ByteArray(value.size + kCCBlockSizeAES128.toInt())
            val dataOutMoved = alloc<ULongVar>()
            val cryptStatus = CCCrypt(
                kCCEncrypt,
                kCCAlgorithmAES,
                kCCOptionPKCS7Padding,
                key.refTo(0),
                kCCKeySizeAES256.toULong(),
                iv.refTo(0),
                value.refTo(0), value.size.convert(),
                dataOut.refTo(0), dataOut.size.convert(),
                dataOutMoved.ptr
            )

            if (cryptStatus != kCCSuccess) {
                throw IllegalStateException("Encryption failed: $cryptStatus")
            }

            return AesEncryptedData(
                value = dataOut.copyOf(dataOutMoved.value.convert()),
                iv = iv,
                salt = salt
            )
        }
    }

    override suspend fun decrypt(data: AesEncryptedData, keyMaterial: ByteArray): ByteArray {
        val key = deriveKey(keyMaterial, data.salt)

        memScoped {
            val dataOut = ByteArray(data.value.size)
            val dataOutMoved = alloc<ULongVar>()

            val cryptStatus = CCCrypt(
                kCCDecrypt,
                kCCAlgorithmAES,
                kCCOptionPKCS7Padding,
                key.refTo(0),
                kCCKeySizeAES256.toULong(),
                data.iv.refTo(0),
                data.value.refTo(0),
                data.value.size.convert(),
                dataOut.refTo(0),
                dataOut.size.convert(),
                dataOutMoved.ptr
            )

            if (cryptStatus != kCCSuccess) {
                throw IllegalStateException("Decryption failed: $cryptStatus")
            }

            return dataOut.copyOf(dataOutMoved.value.convert())
        }
    }

    private fun deriveKey(keyMaterial: ByteArray, salt: ByteArray): ByteArray = TODO()
}
