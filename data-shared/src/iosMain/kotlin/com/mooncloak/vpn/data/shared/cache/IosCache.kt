package com.mooncloak.vpn.data.shared.cache

import kotlinx.datetime.Clock
import kotlinx.serialization.StringFormat
import kotlin.time.Duration

public actual fun Cache.Companion.create(
    format: StringFormat,
    maxSize: Int?,
    expirationAfterWrite: Duration?,
    clock: Clock
): Cache = TODO()
