package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

public actual val Clock.System.elapsedTime: Duration
    get() = System.currentTimeMillis().milliseconds
