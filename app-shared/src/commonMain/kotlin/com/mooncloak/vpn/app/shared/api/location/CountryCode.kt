package com.mooncloak.vpn.app.shared.api.location

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a [ISO 3166-1 Alpha 2](https://en.wikipedia.org/wiki/ISO_3166-1) country code value.
 *
 * @property [value] The actual [String] ISO 3166-1 Alpha 2 code [String] value.
 *
 * @see [ISO 3166-1 Alpha 2](https://en.wikipedia.org/wiki/ISO_3166-1)
 */
@Immutable
@Serializable
@JvmInline
public value class CountryCode public constructor(public val value: String)
