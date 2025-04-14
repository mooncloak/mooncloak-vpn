package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
public data class TypedColorSwatch public constructor(
    @SerialName(value = "swatch") public val swatch: ColorSwatch,
    @SerialName(value = "type") public val type: Type,
    @SerialName(value = "mode") public val mode: Mode? = null
) {

    @JvmInline
    @Serializable
    public value class Type public constructor(
        public val value: String
    ) {

        public companion object {

            public val Dominant: Type = Type(value = "dominant")

            public val Vibrant: Type = Type(value = "vibrant")

            public val Muted: Type = Type(value = "muted")
        }
    }

    @JvmInline
    @Serializable
    public value class Mode public constructor(
        public val value: String
    ) {

        public companion object {

            public val Light: Mode = Mode(value = "light")

            public val Dark: Mode = Mode(value = "dark")
        }
    }
}
