package com.mooncloak.vpn.app.shared.util.time

import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.duration_remaining_days
import com.mooncloak.vpn.app.shared.resource.duration_remaining_greater_than_one_year
import com.mooncloak.vpn.app.shared.resource.duration_remaining_hours
import com.mooncloak.vpn.app.shared.resource.duration_remaining_less_than_one_minute
import com.mooncloak.vpn.app.shared.resource.duration_remaining_minutes
import com.mooncloak.vpn.app.shared.resource.duration_remaining_months
import com.mooncloak.vpn.app.shared.resource.global_not_available
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import com.mooncloak.vpn.util.shared.time.DurationFormatter
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.minutes

/**
 * Retrieves a [DurationFormatter] that formats a [Duration] as an amount of remaining time. If the [Duration] is zero
 * or in the past, then the provided [expired] function will be invoked with the absolute value of the [Duration] to
 * obtain the text to show.
 *
 * @param [expired] The function to invoke to obtain the [String] value when the [Duration] is considered to be expired
 * (less than or equal to zero). Defaults to a string resource value.
 *
 * @return A [DurationFormatter] that formats [Duration]s representing remaining time values.
 */
public fun DurationFormatter.Companion.remaining(
    expired: suspend (duration: Duration) -> String = { getString(Res.string.global_not_available) }
): DurationFormatter = RemainingDurationFormatter(expired = expired)

internal class RemainingDurationFormatter internal constructor(
    private val expired: suspend (duration: Duration) -> String
) : DurationFormatter {

    override suspend fun format(duration: Duration): String =
        if (duration.isPositive() && duration > Duration.ZERO) {
            when {
                duration < 1.minutes -> getString(Res.string.duration_remaining_less_than_one_minute)

                duration < 60.minutes -> getPluralString(
                    Res.plurals.duration_remaining_minutes,
                    duration.inWholeMinutes.toInt(),
                    duration.inWholeMinutes.toInt()
                )

                duration < 1.days -> getPluralString(
                    Res.plurals.duration_remaining_hours,
                    duration.inWholeHours.toInt(),
                    duration.inWholeHours.toInt()
                )

                duration < 30.days -> getPluralString(
                    Res.plurals.duration_remaining_days,
                    duration.inWholeDays.toInt(),
                    duration.inWholeDays.toInt()
                )

                // Close enough for a conversion.
                duration < 365.days -> getPluralString(
                    Res.plurals.duration_remaining_months,
                    (duration.inWholeDays / 30).toInt(),
                    (duration.inWholeDays / 30).toInt()
                )

                else -> getString(Res.string.duration_remaining_greater_than_one_year)
            }
        } else {
            expired.invoke(duration.absoluteValue)
        }
}
