package com.mooncloak.vpn.app.shared.api.server

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.key.Base64Key
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ClientRegistrationResponseBody public constructor(
    @SerialName(value = "registered_public_key") public val registeredPublicKey: Base64Key
)
