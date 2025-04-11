package com.mooncloak.vpn.app.shared.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.Foundation.dataWithBytes
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
public fun NSData.toByteArray(): ByteArray {
    require(this.length <= UInt.MAX_VALUE)

    return ByteArray(this.length.toInt()).apply {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}

public fun NSString.encodeToNSData(): NSData? = this.dataUsingEncoding(NSUTF8StringEncoding)

@OptIn(ExperimentalForeignApi::class)
public fun String.encodeToNSData(): NSData? = memScoped {
    val bytes = this@encodeToNSData.encodeToByteArray()
    val bytePtr = bytes.toCValues().getPointer(this)

    return NSData.dataWithBytes(bytes = bytePtr, bytes.size.toULong())
}
