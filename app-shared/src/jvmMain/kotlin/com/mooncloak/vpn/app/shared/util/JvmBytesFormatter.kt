package com.mooncloak.vpn.app.shared.util

import java.math.RoundingMode
import java.text.DecimalFormat

public actual val BytesFormatter.Companion.Default: BytesFormatter
    get() = JvmBytesFormatter

internal object JvmBytesFormatter : BytesFormatter {

    private val format = DecimalFormat.getInstance().apply {
        roundingMode = RoundingMode.UP
        minimumFractionDigits = 0
        maximumFractionDigits = 0
        minimumIntegerDigits = 1
    }

    override fun format(bytes: Long, type: BytesFormatter.Type): String {
        val value = when (type) {
            BytesFormatter.Type.Bytes -> bytes
            BytesFormatter.Type.Kilobytes -> bytes / 1024
            BytesFormatter.Type.Megabytes -> bytes / (1024 * 2)
            BytesFormatter.Type.Gigabytes -> bytes / (1024 * 3)
            BytesFormatter.Type.Terabytes -> bytes / (1024 * 4)
        }

        return format.format(value)
    }
}
