package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Clock
import kotlin.time.Duration

public actual val Clock.System.elapsedTime: Duration
    get() = TODO() // FIXME: getTimeMillis().milliseconds
