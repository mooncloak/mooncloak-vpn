package com.mooncloak.vpn.app.shared.util

import java.math.RoundingMode
import java.text.DecimalFormat

public actual val DataFormatter.Companion.Default: DataFormatter
    get() = AndroidDataFormatter

internal object AndroidDataFormatter : DataFormatter {

    private val format = DecimalFormat.getInstance().apply {
        roundingMode = RoundingMode.UP
        minimumFractionDigits = 0
        maximumFractionDigits = 0
        minimumIntegerDigits = 1
    }

    override fun format(
        value: Long,
        inputType: DataFormatter.Type,
        outputType: DataFormatter.Type
    ): String {
        if (inputType is DataFormatter.Type.BytesBased && outputType !is DataFormatter.Type.BytesBased) {
            throw IllegalArgumentException("'inputType' and 'outputType' must both be either bytes based or bits based.")
        }
        if (inputType is DataFormatter.Type.BitsBased && outputType !is DataFormatter.Type.BitsBased) {
            throw IllegalArgumentException("'inputType' and 'outputType' must both be either bytes based or bits based.")
        }

        val bytesOrBits = when (inputType) {
            DataFormatter.Type.Bytes -> value.toFloat()
            DataFormatter.Type.Kilobytes -> value * 1024f
            DataFormatter.Type.Megabytes -> value * (1024f * 2)
            DataFormatter.Type.Gigabytes -> value * (1024f * 3)
            DataFormatter.Type.Terabytes -> value * (1024f * 4)
            DataFormatter.Type.Bits -> value.toFloat()
            DataFormatter.Type.Gigabits -> value * 1024f
            DataFormatter.Type.Kilobits -> value * (1024f * 2)
            DataFormatter.Type.Megabits -> value * (1024f * 3)
            DataFormatter.Type.Terabits -> value * (1024f * 4)
        }

        val result = when (outputType) {
            DataFormatter.Type.Bytes -> bytesOrBits
            DataFormatter.Type.Kilobytes -> bytesOrBits / 1024f
            DataFormatter.Type.Megabytes -> bytesOrBits / (1024f * 2)
            DataFormatter.Type.Gigabytes -> bytesOrBits / (1024f * 3)
            DataFormatter.Type.Terabytes -> bytesOrBits / (1024f * 4)
            DataFormatter.Type.Bits -> bytesOrBits
            DataFormatter.Type.Gigabits -> bytesOrBits / 1024f
            DataFormatter.Type.Kilobits -> bytesOrBits / (1024f * 2)
            DataFormatter.Type.Megabits -> bytesOrBits / (1024f * 3)
            DataFormatter.Type.Terabits -> bytesOrBits / (1024f * 4)
        }

        return when {
            result == 0f -> "0"
            result < 1f -> "< 1"
            else -> format.format(result)
        }
    }
}
