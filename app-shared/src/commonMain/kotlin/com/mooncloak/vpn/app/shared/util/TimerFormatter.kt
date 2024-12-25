package com.mooncloak.vpn.app.shared.util

import kotlin.time.Duration

public interface TimerFormatter {

    public fun format(duration: Duration): String

    public companion object
}

public val TimerFormatter.Companion.Default: TimerFormatter
    get() = DefaultTimerFormatter

/**
 * A default [TimerFormatter] Formatter implementation that converts a provided [Duration] value to a colon separated
 * time stamp [String]. For example, a duration of 1 hour, 30 minutes, and 10 seconds would be converted to the
 * following [String] value: "01:30:10". The resulting [String] value will:
 *     - Always have two digits for each part (ex: seconds), if a part value is less than ten, then a leading zero will
 *     be added so that it contains two digits.
 *     - Display at least two parts (minutes and seconds). If the provided [Duration] represents less than a minute, a
 *     part of "00" will be added for the minutes.
 *     - Display at most three parts (hours, minutes, and seconds) in this described format.
 *     - If the provided [Duration] exceeds 24 hours, then the resulting [String] value will be the result of
 *     [Duration.toString].
 */
internal data object DefaultTimerFormatter : TimerFormatter {

    override fun format(duration: Duration): String {
        val totalSeconds = duration.inWholeSeconds
        val totalMinutes = duration.inWholeMinutes
        val totalHours = duration.inWholeHours

        val secondsInMinute = totalSeconds % 60L
        val minutesInHour = totalMinutes % 60L

        return when {
            totalSeconds < 10L -> "00:00:0$totalSeconds"
            totalSeconds < 60L -> "00:00:$totalSeconds"
            totalMinutes < 10L && secondsInMinute < 10L -> "00:0$totalMinutes:0$secondsInMinute"
            totalMinutes < 10L -> "00:0$totalMinutes:$secondsInMinute"
            totalMinutes < 60L && secondsInMinute < 10L -> "00:$totalMinutes:0$secondsInMinute"
            totalMinutes < 60L -> "00:$totalMinutes:$secondsInMinute"
            totalHours < 10L && minutesInHour < 10L && secondsInMinute < 10L -> "0$totalHours:0$minutesInHour:0$secondsInMinute"
            totalHours < 10L && minutesInHour < 10L -> "0$totalHours:0$minutesInHour:$secondsInMinute"
            totalHours < 10L && secondsInMinute < 10L -> "0$totalHours:$minutesInHour:0$secondsInMinute"
            totalHours < 10L -> "0$totalHours:$minutesInHour:$secondsInMinute"
            totalHours < 24L && minutesInHour < 10L && secondsInMinute < 10L -> "$totalHours:0$minutesInHour:0$secondsInMinute"
            totalHours < 24L && minutesInHour < 10L -> "$totalHours:0$minutesInHour:$secondsInMinute"
            totalHours < 24L && secondsInMinute < 10L -> "$totalHours:$minutesInHour:0$secondsInMinute"
            totalHours < 24L -> "$totalHours:$minutesInHour:$secondsInMinute"
            else -> duration.toIsoString()
        }
    }
}
