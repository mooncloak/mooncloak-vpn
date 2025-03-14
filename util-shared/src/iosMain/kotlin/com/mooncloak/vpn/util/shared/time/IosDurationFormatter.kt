package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Clock
import platform.Foundation.NSDate
import platform.Foundation.NSFormattingContextStandalone
import platform.Foundation.NSRelativeDateTimeFormatter
import platform.Foundation.NSRelativeDateTimeFormatterStyleNamed
import platform.Foundation.dateWithTimeIntervalSinceNow
import platform.Foundation.now

public actual fun DurationFormatter.Companion.relative(
    clock: Clock
): DurationFormatter = DurationFormatter { duration ->
    // Convert Duration to seconds (NSTimeInterval is a Double in seconds)
    val seconds = duration.inWholeMilliseconds / 1000.0
    val targetDate = NSDate.dateWithTimeIntervalSinceNow(seconds)
    val now = NSDate.now()

    val formatter = NSRelativeDateTimeFormatter().apply {
        dateTimeStyle = NSRelativeDateTimeFormatterStyleNamed // "2 hours ago"
        formattingContext = NSFormattingContextStandalone
        // Locale is automatically the user's current locale
    }

    formatter.localizedStringForDate(targetDate, now)
}
