package com.mooncloak.vpn.api.shared.token

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a specific [Token] value representing a successful transaction.
 *
 * @property [value] The transaction token [String] value.
 */
@Immutable
@Serializable
@JvmInline
public value class TransactionToken public constructor(
    public val value: String
)
