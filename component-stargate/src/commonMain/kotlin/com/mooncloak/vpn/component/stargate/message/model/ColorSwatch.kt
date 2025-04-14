package com.mooncloak.vpn.component.stargate.message.model

import androidx.compose.ui.graphics.Color
import com.mooncloak.kodetools.compose.serialization.ColorAsRgbaObjectSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
public data class ColorSwatch public constructor(
    @SerialName(value = "color") @Serializable(with = ColorAsRgbaObjectSerializer::class) public val color: Color,
    @SerialName(value = "primary_on_color") @Serializable(with = ColorAsRgbaObjectSerializer::class) public val primaryOnColor: Color,
    @SerialName(value = "secondary_on_color") @Serializable(with = ColorAsRgbaObjectSerializer::class) public val secondaryOnColor: Color,
    @SerialName(value = "population") public val population: Int? = null
)
