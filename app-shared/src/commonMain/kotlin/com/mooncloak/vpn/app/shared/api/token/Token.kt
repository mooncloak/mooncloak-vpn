package com.mooncloak.vpn.app.shared.api.token

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@JvmInline
public value class Token public constructor(
    public val value: String
)
