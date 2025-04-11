package com.mooncloak.vpn.util.shared.crypto

import org.kotlincrypto.random.CryptoRand
import kotlin.random.Random

/**
 * Represents a cryptographically secure [Random] (csprng) implementation that can be used for
 * cryptographic functions.
 *
 * > [!Warning]
 * > While this implementation is considered secure, it is not meant to be used outside the scope
 * > of this library, and no guarantees are made about its usage or implementation.
 *
 * @see [Random.Default.Secure] to obtain an instance of this class.
 */
public class SecureRandom internal constructor(
    private val nextSecureRandomBytes: (byteCount: Int) -> ByteArray
) : Random() {

    override fun nextBits(bitCount: Int): Int {
        require(bitCount in 0..32) { "'bitCount' property must be in the range 0 to 32." }

        if (bitCount == 0) return 0

        val bytes = nextSecureRandomBytes(bitCount.bytesPerBitCount())

        return bytes.toInt(startInclusive = 0, endExclusive = 4)
            .takeUpperBits(bitCount)
    }
}

/**
 * Retrieves a [SecureRandom] that can be used for cryptographic purposes.
 */
public val Random.Default.Secure: SecureRandom
    get() = defaultSecureRandomSingleton

private val defaultSecureRandomSingleton = SecureRandom(
    nextSecureRandomBytes = { count ->
        val bytes = ByteArray(count)
        delegateSecureRandom.nextBytes(bytes)
    })

private val delegateSecureRandom = CryptoRand.Default

/**
 * Represents the order of a "word" or a numeric value that consists of multiple bytes. In [Little] Endian, the least
 * significant (or last) byte is stored first. In [Big] Endian, the most significant (or first) byte is stored first.
 *
 * Note that technically, the endianness is abstracted away from Kotlin, and each target platform may represent the
 * values using their desired endian order. However, sometimes when working with [ByteArray]s, it is required to
 * convert them to numeric values, such as [Int] or [Long], so, in those scenarios, it is required to specify the order
 * of how the [Byte]s are converted to the numerical value. Though this conversion is different from how the value is
 * internally stored or represented, it will result in different numeric values depending on the order.
 *
 * @see [Wikipedia Explanation](https://en.wikipedia.org/wiki/Endianness)
 */
internal enum class Endian {

    Big,
    Little
}

/**
 * Converts the specified indices of this [ByteArray] into an [Int] value in the provider [order].
 *
 * Note that [Int]s are 32-bit (4 byte) values. An exception will be thrown if the index range is greater than four
 * or [startInclusive] is greater than or equal to [endExclusive].
 *
 * @param [startInclusive] The first index to use in the conversion process of this [ByteArray] to an [Int]. This
 * defaults to zero.
 * @param [endExclusive] The last, not included, index used in the conversion process of this [ByteArray] to an [Int].
 * This defaults to [ByteArray.size].
 * @param [order] The [Endian] order to use for the conversion process. This defaults to [Endian.Big]. Note that the
 * resulting [Int] value will be different for [Endian.Big] and [Endian.Little] order for the same [ByteArray].
 */
internal fun ByteArray.toInt(
    startInclusive: Int = 0,
    endExclusive: Int = size,
    order: Endian = Endian.Big
): Int {
    require(endExclusive > startInclusive) { "startInclusive value must be less than endExclusive value." }
    require((endExclusive - startInclusive) < 5) { "Cannot convert more than 4 bytes to an Int, as an Int value is 32-bits (4 bytes)." }
    require(startInclusive in indices) { "startInclusive value must be within the ByteArray indices range." }
    require(endExclusive in 0..size) { "endExclusive value must not be greater than the size of the ByteArray and must not be less than zero." }

    var result = 0

    if (order == Endian.Little) {
        var shift = 0

        for (i in startInclusive until endExclusive) {
            result = result or ((this[i].toInt() and 0xFF) shl shift)
            shift += 8
        }
    } else {
        var shift = 24

        for (i in startInclusive until endExclusive) {
            result = result or ((this[i].toInt() and 0xFF) shl shift)
            shift -= 8
        }
    }

    return result
}

/**
 * Takes upper [bitCount] bits (0..32) from this number.
 *
 * **Note:** This is taken from the Kotlin [Random] implementation.
 */
internal fun Int.takeUpperBits(bitCount: Int): Int =
    this.ushr(32 - bitCount) and (-bitCount).shr(31)

/**
 * Returns the amount of bytes necessary to contain [this] amount of bits.
 */
internal fun Int.bytesPerBitCount(): Int =
    if (this % 8 != 0) {
        (this / 8) + 1
    } else {
        this / 8
    }
