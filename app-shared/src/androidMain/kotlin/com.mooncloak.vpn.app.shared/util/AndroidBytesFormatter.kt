package com.mooncloak.vpn.app.shared.util

import java.math.RoundingMode
import java.text.DecimalFormat

public actual val BytesFormatter.Companion.Default: BytesFormatter
    get() = AndroidBytesFormatter

internal object AndroidBytesFormatter : BytesFormatter {

    private val format = DecimalFormat.getInstance().apply {
        roundingMode = RoundingMode.UP
        minimumFractionDigits = 0
        maximumFractionDigits = 0
        minimumIntegerDigits = 1
    }

    override fun format(bytes: Long, type: BytesFormatter.Type): String {
        val value = when (type) {
            BytesFormatter.Type.Bytes -> bytes.toFloat()
            BytesFormatter.Type.Kilobytes -> bytes / 1024f
            BytesFormatter.Type.Megabytes -> bytes / (1024f * 2)
            BytesFormatter.Type.Gigabytes -> bytes / (1024f * 3)
            BytesFormatter.Type.Terabytes -> bytes / (1024f * 4)
        }

        return when {
            value == 0f -> "0"
            value < 1f -> "< 1"
            else -> format.format(value)
        }
    }
}
