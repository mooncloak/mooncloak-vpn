package com.mooncloak.vpn.app.shared.api.server

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ClientRegistrationResponseBody public constructor(
    @SerialName(value = "server_public_key") public val serverPublicKey: String
)
