package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator(discriminator = "@type")
@OptIn(ExperimentalSerializationApi::class)
public sealed class Emoji {

    @SerialName(value = "@context")
    public val context: String = "https://mooncloak.com/types"
}

@Serializable
@SerialName(value = "unicode")
public data class UnicodeEmoji public constructor(
    @SerialName(value = "value") public val value: String
) : Emoji()

@Serializable
@SerialName(value = "image")
public data class ImageEmoji public constructor(
    @SerialName(value = "value") public val value: ImageContent
) : Emoji()
