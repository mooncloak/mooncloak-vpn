package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_bps
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_bytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_gbps
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_gigabytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_kbps
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_kilobytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_mbps
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_megabytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_tbps
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_terabytes
import org.jetbrains.compose.resources.stringResource

public fun interface DataFormatter {

    public fun format(
        value: Long,
        inputType: Type,
        outputType: Type
    ): String

    public sealed interface Type {

        public sealed interface BytesBased : Type

        public sealed interface BitsBased : Type

        public data object Bytes : BytesBased
        public data object Kilobytes : BytesBased
        public data object Megabytes : BytesBased
        public data object Gigabytes : BytesBased
        public data object Terabytes : BytesBased

        public data object Bits : BitsBased
        public data object Kilobits : BitsBased
        public data object Megabits : BitsBased
        public data object Gigabits : BitsBased
        public data object Terabits : BitsBased
    }

    public companion object
}

public expect val DataFormatter.Companion.Default: DataFormatter

public val DataFormatter.Type.abbreviatedText: String
    @Composable
    get() = when (this) {
        DataFormatter.Type.Bytes -> stringResource(Res.string.data_format_abbr_bytes)
        DataFormatter.Type.Kilobytes -> stringResource(Res.string.data_format_abbr_kilobytes)
        DataFormatter.Type.Megabytes -> stringResource(Res.string.data_format_abbr_megabytes)
        DataFormatter.Type.Gigabytes -> stringResource(Res.string.data_format_abbr_gigabytes)
        DataFormatter.Type.Terabytes -> stringResource(Res.string.data_format_abbr_terabytes)
        DataFormatter.Type.Bits -> stringResource(Res.string.data_format_abbr_bps)
        DataFormatter.Type.Kilobits -> stringResource(Res.string.data_format_abbr_kbps)
        DataFormatter.Type.Megabits -> stringResource(Res.string.data_format_abbr_mbps)
        DataFormatter.Type.Gigabits -> stringResource(Res.string.data_format_abbr_gbps)
        DataFormatter.Type.Terabits -> stringResource(Res.string.data_format_abbr_tbps)
    }

@Composable
public fun DataFormatter.formatWithUnit(
    value: Long,
    inputType: DataFormatter.Type,
    outputType: DataFormatter.Type
): String {
    val formattedValue = format(
        value = value,
        inputType = inputType,
        outputType = outputType
    )
    val unit = outputType.abbreviatedText

    return "$formattedValue $unit" // TODO: Localize?
}
