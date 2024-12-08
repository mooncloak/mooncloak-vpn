package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal data class RevokedTokenResponseBody internal constructor(
    @SerialName(value = "success") val success: Boolean = true
)
