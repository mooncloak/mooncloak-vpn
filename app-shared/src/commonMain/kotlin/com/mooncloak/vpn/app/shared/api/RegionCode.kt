package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Represents a [ISO 3166-2](https://en.wikipedia.org/wiki/ISO_3166-1) region (ex: US State) code value.
 *
 * @property [value] The actual [String] ISO 3166-2 language code [String] value.
 *
 * @see [ISO 3166-1 Alpha 2](https://en.wikipedia.org/wiki/ISO_3166-2)
 */
@Immutable
@Serializable
@JvmInline
public value class RegionCode public constructor(public val value: String)
