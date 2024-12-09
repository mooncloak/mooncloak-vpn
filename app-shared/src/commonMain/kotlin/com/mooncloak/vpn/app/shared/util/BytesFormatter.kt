package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_bytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_gigabytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_kilobytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_megabytes
import com.mooncloak.vpn.app.shared.resource.data_format_abbr_terabytes
import org.jetbrains.compose.resources.stringResource

public fun interface BytesFormatter {

    public fun format(bytes: Long, type: Type): String

    public enum class Type {

        Bytes,
        Kilobytes,
        Megabytes,
        Gigabytes,
        Terabytes
    }

    public companion object
}

public expect val BytesFormatter.Companion.Default: BytesFormatter

public val BytesFormatter.Type.abbreviatedText: String
    @Composable
    get() = when (this) {
        BytesFormatter.Type.Bytes -> stringResource(Res.string.data_format_abbr_bytes)
        BytesFormatter.Type.Kilobytes -> stringResource(Res.string.data_format_abbr_kilobytes)
        BytesFormatter.Type.Megabytes -> stringResource(Res.string.data_format_abbr_megabytes)
        BytesFormatter.Type.Gigabytes -> stringResource(Res.string.data_format_abbr_gigabytes)
        BytesFormatter.Type.Terabytes -> stringResource(Res.string.data_format_abbr_terabytes)
    }

@Composable
public fun BytesFormatter.formatWithUnit(bytes: Long, type: BytesFormatter.Type): String {
    val formattedValue = format(bytes = bytes, type = type)
    val unit = type.abbreviatedText

    return "$formattedValue$unit" // TODO: Localize?
}
