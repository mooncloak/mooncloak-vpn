package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

/**
 * Represents a compacted image placeholder.
 *
 * @property [value] The actual blur hash value.
 *
 * @see [blurha.sh Website](https://blurha.sh/)
 * @see [blurhash Repository](https://github.com/woltapp/blurhash)
 */
@JvmInline
@Serializable
public value class BlurHash public constructor(
    public val value: String
)
