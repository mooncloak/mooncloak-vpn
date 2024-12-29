package com.mooncloak.vpn.app.shared.util.time

import kotlinx.datetime.Clock
import kotlin.time.Duration

public expect val Clock.System.elapsedTime: Duration
