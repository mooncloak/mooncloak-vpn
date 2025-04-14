package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a grouping of [ColorSwatch]es. For example, colors can be extracted from an image to generate a color
 * palette from the image.
 */
@Serializable
public data class ColorPalette public constructor(
    @SerialName(value = "swatches") public val swatches: List<TypedColorSwatch>
)

public operator fun ColorPalette.invoke(
    dominantSwatch: ColorSwatch? = null,
    vibrantLightSwatch: ColorSwatch? = null,
    vibrantDarkSwatch: ColorSwatch? = null,
    vibrantSwatch: ColorSwatch? = null,
    mutedLightSwatch: ColorSwatch? = null,
    mutedDarkSwatch: ColorSwatch? = null,
    mutedSwatch: ColorSwatch? = null
): ColorPalette = ColorPalette(
    swatches = listOfNotNull(
        dominantSwatch?.let { swatch ->
            TypedColorSwatch(
                swatch = swatch,
                type = TypedColorSwatch.Type.Dominant,
                mode = null
            )
        },
        vibrantLightSwatch?.let { swatch ->
            TypedColorSwatch(
                swatch = swatch,
                type = TypedColorSwatch.Type.Vibrant,
                mode = TypedColorSwatch.Mode.Light
            )
        },
        vibrantDarkSwatch?.let { swatch ->
            TypedColorSwatch(
                swatch = swatch,
                type = TypedColorSwatch.Type.Vibrant,
                mode = TypedColorSwatch.Mode.Dark
            )
        },
        vibrantSwatch?.let { swatch ->
            TypedColorSwatch(
                swatch = swatch,
                type = TypedColorSwatch.Type.Vibrant,
                mode = null
            )
        },
        mutedLightSwatch?.let { swatch ->
            TypedColorSwatch(
                swatch = swatch,
                type = TypedColorSwatch.Type.Muted,
                mode = TypedColorSwatch.Mode.Light
            )
        },
        mutedDarkSwatch?.let { swatch ->
            TypedColorSwatch(
                swatch = swatch,
                type = TypedColorSwatch.Type.Muted,
                mode = TypedColorSwatch.Mode.Dark
            )
        },
        mutedSwatch?.let { swatch ->
            TypedColorSwatch(
                swatch = swatch,
                type = TypedColorSwatch.Type.Muted,
                mode = null
            )
        }
    )
)
