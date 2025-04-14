package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator(discriminator = "@type")
@OptIn(ExperimentalSerializationApi::class)
public sealed class MediaSource {

    @SerialName(value = "@context")
    public val context: String = "https://mooncloak.com/types"

    public companion object
}

@Serializable
@SerialName(value = "EmbeddedMediaSource")
public data class EmbeddedMediaSource public constructor(
    public val value: ByteArray
) : MediaSource() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmbeddedMediaSource) return false

        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int = value.contentHashCode()
}

@Serializable
@SerialName(value = "ReferencedMediaSource")
public data class ReferencedMediaSource public constructor(
    public val uri: String
) : MediaSource()
