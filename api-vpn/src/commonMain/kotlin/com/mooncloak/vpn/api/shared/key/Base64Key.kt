package com.mooncloak.vpn.api.shared.key

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a WireGuard Key in Base64 [String] format. This value is serialized as a [String], but is provided for
 * type-safety.
 */
@JvmInline
@Serializable
public value class Base64Key public constructor(
    public val value: String
)
