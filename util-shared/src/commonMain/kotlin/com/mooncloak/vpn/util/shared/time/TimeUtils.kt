package com.mooncloak.vpn.util.shared.time

import kotlinx.datetime.Clock
import kotlin.time.Duration

public expect val Clock.System.elapsedTime: Duration
