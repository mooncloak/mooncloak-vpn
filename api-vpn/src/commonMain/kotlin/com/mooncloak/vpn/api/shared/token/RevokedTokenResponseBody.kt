package com.mooncloak.vpn.api.shared.token

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class RevokedTokenResponseBody public constructor(
    @SerialName(value = "success") val success: Boolean = true
)
