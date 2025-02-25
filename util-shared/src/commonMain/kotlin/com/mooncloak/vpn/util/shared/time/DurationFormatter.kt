package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Clock

import kotlin.time.Duration

public fun interface DurationFormatter {

    public suspend fun format(duration: Duration): String

    public companion object
}

/**
 * Retrieves a [DurationFormatter] that formats a [Duration] into an ISO-8601 duration [String] value using the
 * [Duration.toIsoString] function.
 */
public val DurationFormatter.Companion.Iso8601: DurationFormatter
    get() = Iso8601DurationFormatter

/**
 * Retrieves a [DurationFormatter] that formats a [Duration] relative to the current time.
 *
 * @param [clock] The [Clock] to use to obtain the current timestamp to use in the formatting of the relative
 * durations.
 *
 * @return A [DurationFormatter] that formats [Duration] values relative to the current time.
 */
public expect fun DurationFormatter.Companion.relative(
    clock: Clock = Clock.System
): DurationFormatter

internal data object Iso8601DurationFormatter : DurationFormatter {

    override suspend fun format(duration: Duration): String = duration.toIsoString()
}
