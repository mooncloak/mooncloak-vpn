package com.mooncloak.vpn.util.shared.time

import android.os.Build
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaZoneId
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

public actual val DateTimeFormatter.Companion.Full: DateTimeFormatter
    get() = FullDateTimeFormatter

internal data object FullDateTimeFormatter : AndroidDateTimeFormatter(
    pattern = "hh:mma EEE, MMM dd, yyyy",
    locale = Locale.getDefault()
)

internal open class AndroidDateTimeFormatter internal constructor(
    private val pattern: String,
    private val locale: Locale
) : DateTimeFormatter {

    private var formatter: java.time.format.DateTimeFormatter? = null
    private val simpleFormatter = SimpleDateFormat(pattern, locale)

    override fun format(instant: Instant, timeZone: TimeZone): String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // We have to instantiate the DateTimeFormatter within the bounds of the Android version check. So, we
            // store the version after creating it for faster loads for subsequent invocations of this function.
            val formatter = (this.formatter ?: java.time.format.DateTimeFormatter.ofPattern(pattern, locale))
                .also { this.formatter = it }

            val zoned = instant.toJavaInstant().atZone(timeZone.toJavaZoneId())

            formatter.format(zoned)
        } else {
            simpleFormatter.format(Date(instant.toEpochMilliseconds()))
        }
}
