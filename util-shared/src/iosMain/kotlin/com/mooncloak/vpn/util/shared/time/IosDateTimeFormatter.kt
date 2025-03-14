package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toNSDate
import platform.Foundation.*

public actual val DateTimeFormatter.Companion.Full: DateTimeFormatter
    get() = FullDateTimeFormatter

internal object FullDateTimeFormatter : DateTimeFormatter {

    private var formatter: NSDateFormatter? = null

    override fun format(instant: Instant, timeZone: TimeZone): String {
        val formatter = (this.formatter ?: NSDateFormatter().apply {
            dateFormat = "hh:mmaa EEE, MMM dd, yyyy"
            locale = NSLocale.currentLocale
            setTimeZone(NSTimeZone.timeZoneWithName(timeZone.id))
        }).also { this.formatter = it }

        return formatter.stringFromDate(instant.toNSDate())
    }
}
