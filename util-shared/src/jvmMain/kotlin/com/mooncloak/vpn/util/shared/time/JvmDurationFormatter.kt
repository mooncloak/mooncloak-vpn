package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Clock

public actual fun DurationFormatter.Companion.relative(
    clock: Clock
): DurationFormatter = DurationFormatter { duration ->
    // FIXME: Support relative durations in the future.
    duration.toIsoString()
}
