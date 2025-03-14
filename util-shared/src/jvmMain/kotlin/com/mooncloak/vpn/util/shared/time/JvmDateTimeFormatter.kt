package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.util.Locale

public actual val DateTimeFormatter.Companion.Full: DateTimeFormatter
    get() = FullDateTimeFormatter

internal data object FullDateTimeFormatter : JvmDateTimeFormatter(
    pattern = "hh:mma EEE, MMM dd, yyyy",
    locale = Locale.getDefault()
)

internal open class JvmDateTimeFormatter internal constructor(
    private val pattern: String,
    private val locale: Locale
) : DateTimeFormatter {

    private var formatter: java.time.format.DateTimeFormatter? = null

    override fun format(instant: Instant, timeZone: TimeZone): String {
        // We have to instantiate the DateTimeFormatter within the bounds of the Android version check. So, we
        // store the version after creating it for faster loads for subsequent invocations of this function.
        val formatter = (this.formatter ?: java.time.format.DateTimeFormatter.ofPattern(pattern, locale))
            .also { this.formatter = it }

        val zoned = instant.toJavaInstant().atZone(timeZone.toJavaZoneId())

        return formatter.format(zoned)
    }
}
