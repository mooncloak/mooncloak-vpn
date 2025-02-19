package com.mooncloak.vpn.app.shared.util.time

import kotlinx.datetime.Clock

public actual fun DurationFormatter.Companion.relative(
    clock: Clock
): DurationFormatter = DurationFormatter { duration ->
    // FIXME: Support relative durations in the future.
    duration.toIsoString()
}
