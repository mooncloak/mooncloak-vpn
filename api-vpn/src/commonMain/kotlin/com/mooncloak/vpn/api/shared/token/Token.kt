package com.mooncloak.vpn.api.shared.token

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a type-safe wrapper around a token [String] value, such as an Access Token, Refresh Token, or API Token.
 *
 * @property [value] The actual token [String] value.
 */
@Immutable
@Serializable
@JvmInline
public value class Token public constructor(
    public val value: String
)
