package com.mooncloak.vpn.api.shared.token

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class RevokeTokenRequestBody public constructor(
    @SerialName(value = "token") val token: Token,
    @SerialName(value = "hint") val hint: TokenTypeHint? = null
)
