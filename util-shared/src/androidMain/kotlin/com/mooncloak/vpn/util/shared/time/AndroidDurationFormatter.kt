package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Clock

// TODO: Implement Android relative DurationFormatter
public actual fun DurationFormatter.Companion.relative(
    clock: Clock
): DurationFormatter = DurationFormatter { duration -> duration.toIsoString() }
