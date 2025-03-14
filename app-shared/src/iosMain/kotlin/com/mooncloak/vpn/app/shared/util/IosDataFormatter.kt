package com.mooncloak.vpn.app.shared.util

public actual val DataFormatter.Companion.Default: DataFormatter
    get() = IosDataFormatter

internal object IosDataFormatter : DataFormatter {

    override fun format(value: Long, inputType: DataFormatter.Type, outputType: DataFormatter.Type): String {
        TODO("Not yet implemented")
    }
}
