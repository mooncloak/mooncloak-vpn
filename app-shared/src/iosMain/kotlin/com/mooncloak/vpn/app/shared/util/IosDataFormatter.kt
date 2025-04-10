package com.mooncloak.vpn.app.shared.util

import platform.Foundation.*

public actual val DataFormatter.Companion.Default: DataFormatter
    get() = IosDataFormatter

internal object IosDataFormatter : DataFormatter {

    override fun format(value: Long, inputType: DataFormatter.Type, outputType: DataFormatter.Type): String {
        // Convert input value to bytes as a common base unit
        val bytesValue = when (inputType) {
            is DataFormatter.Type.Bytes -> value.toDouble()
            is DataFormatter.Type.Kilobytes -> value * 1024.0
            is DataFormatter.Type.Megabytes -> value * 1024.0 * 1024.0
            is DataFormatter.Type.Gigabytes -> value * 1024.0 * 1024.0 * 1024.0
            is DataFormatter.Type.Terabytes -> value * 1024.0 * 1024.0 * 1024.0 * 1024.0
            is DataFormatter.Type.Bits -> value / 8.0
            is DataFormatter.Type.Kilobits -> value * 1024.0 / 8.0
            is DataFormatter.Type.Megabits -> value * 1024.0 * 1024.0 / 8.0
            is DataFormatter.Type.Gigabits -> value * 1024.0 * 1024.0 * 1024.0 / 8.0
            is DataFormatter.Type.Terabits -> value * 1024.0 * 1024.0 * 1024.0 * 1024.0 / 8.0
        }

        return when (outputType) {
            is DataFormatter.Type.BytesBased -> formatBytes(bytesValue, outputType)
            is DataFormatter.Type.BitsBased -> formatBits(bytesValue, outputType)
        }
    }

    private fun formatBytes(bytes: Double, outputType: DataFormatter.Type.BytesBased): String {
        val formatter = NSByteCountFormatter().apply {
            countStyle = NSByteCountFormatterCountStyleBinary
            allowsNonnumericFormatting = false
        }

        // Convert to appropriate unit before formatting
        val valueInUnit = when (outputType) {
            DataFormatter.Type.Bytes -> bytes
            DataFormatter.Type.Kilobytes -> bytes / 1024.0
            DataFormatter.Type.Megabytes -> bytes / (1024.0 * 1024.0)
            DataFormatter.Type.Gigabytes -> bytes / (1024.0 * 1024.0 * 1024.0)
            DataFormatter.Type.Terabytes -> bytes / (1024.0 * 1024.0 * 1024.0 * 1024.0)
        }

        return formatter.stringFromByteCount(valueInUnit.toLong())
    }

    private fun formatBits(bytes: Double, outputType: DataFormatter.Type.BitsBased): String {
        // Convert bytes to bits
        val bits = bytes * 8.0

        // Convert to target unit and format
        val (value, unit) = when (outputType) {
            DataFormatter.Type.Bits -> bits to "b"
            DataFormatter.Type.Kilobits -> (bits / 1024.0) to "Kb"
            DataFormatter.Type.Megabits -> (bits / (1024.0 * 1024.0)) to "Mb"
            DataFormatter.Type.Gigabits -> (bits / (1024.0 * 1024.0 * 1024.0)) to "Gb"
            DataFormatter.Type.Terabits -> (bits / (1024.0 * 1024.0 * 1024.0 * 1024.0)) to "Tb"
        }

        // Use NSNumberFormatter for consistent number formatting
        val formatter = NSNumberFormatter().apply {
            minimumFractionDigits = 2u
            maximumFractionDigits = 2u
            paddingCharacter = "0"
        }

        return "${formatter.stringFromNumber(NSNumber(value)) ?: value.toString()} $unit"
    }
}
