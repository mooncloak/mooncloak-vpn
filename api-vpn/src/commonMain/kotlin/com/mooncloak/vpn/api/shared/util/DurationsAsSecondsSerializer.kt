package com.mooncloak.vpn.api.shared.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

public data object DurationAsSecondsSerializer : KSerializer<Duration> {

    override val descriptor: SerialDescriptor
        get() = Long.serializer().descriptor

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeSeconds)
    }

    override fun deserialize(decoder: Decoder): Duration {
        val seconds = decoder.decodeLong()

        return seconds.seconds
    }
}
