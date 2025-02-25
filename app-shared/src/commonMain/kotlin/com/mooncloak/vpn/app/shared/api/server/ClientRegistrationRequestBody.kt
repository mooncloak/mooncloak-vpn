package com.mooncloak.vpn.app.shared.api.server

import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.app.shared.api.key.Base64Key
import com.mooncloak.vpn.api.shared.vpn.VPNProtocol
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ClientRegistrationRequestBody public constructor(
    @SerialName(value = "server_id") public val serverId: String,
    @SerialName(value = "public_key") public val publicKey: Base64Key,
    @SerialName(value = "key_id") public val keyId: String? = null,
    @SerialName(value = "protocol") public val protocol: VPNProtocol? = null
)
