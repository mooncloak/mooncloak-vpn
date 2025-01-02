package com.mooncloak.vpn.app.shared.util.time

import kotlin.time.Duration

public fun interface DurationFormatter {

    public fun format(duration: Duration): String

    public companion object
}

public val DurationFormatter.Companion.Default: DurationFormatter
    get() = DefaultDurationFormatter

internal data object DefaultDurationFormatter : DurationFormatter {

    override fun format(duration: Duration): String = duration.toIsoString()
}
