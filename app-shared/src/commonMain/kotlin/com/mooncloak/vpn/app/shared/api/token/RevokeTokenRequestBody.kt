package com.mooncloak.vpn.app.shared.api.token

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal data class RevokeTokenRequestBody internal constructor(
    @SerialName(value = "token") val token: Token,
    @SerialName(value = "hint") val hint: TokenTypeHint? = null
)
