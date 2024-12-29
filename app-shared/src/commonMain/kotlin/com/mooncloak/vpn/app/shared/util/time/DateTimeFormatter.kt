package com.mooncloak.vpn.app.shared.util.time

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

public fun interface DateFormatter {

    public fun format(instant: Instant, timeZone: TimeZone): String

    public companion object
}

public fun interface TimeFormatter {

    public fun format(instant: Instant, timeZone: TimeZone): String

    public companion object
}

public fun interface DateTimeFormatter : DateFormatter,
    TimeFormatter {

    public override fun format(instant: Instant, timeZone: TimeZone): String

    public companion object
}

public fun DateFormatter.format(instant: Instant): String =
    this.format(instant = instant, timeZone = TimeZone.currentSystemDefault())

public fun DateFormatter.format(date: LocalDate, timeZone: TimeZone = TimeZone.currentSystemDefault()): String =
    this.format(instant = date.atStartOfDayIn(timeZone), timeZone = timeZone)

public fun DateFormatter.format(date: LocalDateTime, timeZone: TimeZone = TimeZone.currentSystemDefault()): String =
    this.format(instant = date.date.atStartOfDayIn(timeZone), timeZone = timeZone)

public fun TimeFormatter.format(instant: Instant): String =
    this.format(instant = instant, timeZone = TimeZone.currentSystemDefault())

public fun TimeFormatter.format(
    time: LocalTime,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    clock: Clock = Clock.System
): String {
    val now = clock.now().toLocalDateTime(timeZone)
    val today = LocalDateTime(
        year = now.year,
        monthNumber = now.monthNumber,
        dayOfMonth = now.dayOfMonth,
        hour = time.hour,
        minute = time.minute,
        second = time.second,
        nanosecond = time.nanosecond
    )

    return this.format(instant = today.toInstant(timeZone), timeZone = timeZone)
}

public fun DateTimeFormatter.format(instant: Instant): String =
    this.format(instant = instant, timeZone = TimeZone.currentSystemDefault())

public expect val DateTimeFormatter.Companion.Full: DateTimeFormatter
