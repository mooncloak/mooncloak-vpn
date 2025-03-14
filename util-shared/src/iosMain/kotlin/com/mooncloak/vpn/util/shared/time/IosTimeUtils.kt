package com.mooncloak.vpn.util.shared.time

import kotlinx.cinterop.memScoped
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlinx.cinterop.*
import platform.posix.*
import kotlin.time.Duration.Companion.nanoseconds

@OptIn(ExperimentalForeignApi::class)
public actual val Clock.System.elapsedTime: Duration
    get() = memScoped {
        val time = alloc<timespec>()
        clock_gettime(CLOCK_MONOTONIC.toUInt(), time.ptr)
        val nanos = time.tv_sec * 1_000_000_000 + time.tv_nsec
        nanos.nanoseconds
    }
