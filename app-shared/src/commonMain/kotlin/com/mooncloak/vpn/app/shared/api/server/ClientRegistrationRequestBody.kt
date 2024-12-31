package com.mooncloak.vpn.app.shared.api.server

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ClientRegistrationRequestBody public constructor(
    @SerialName(value = "public_key") public val publicKey: String
)
