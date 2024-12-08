package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@JvmInline
public value class TransactionToken public constructor(
    public val value: String
)
