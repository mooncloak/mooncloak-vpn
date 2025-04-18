package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.component.stargate.message.model.ImageContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ProfileImages public constructor(
    @SerialName(value = "avatar") public val avatar: ImageContent? = null,
    @SerialName(value = "banner") public val banner: ImageContent? = null
)
