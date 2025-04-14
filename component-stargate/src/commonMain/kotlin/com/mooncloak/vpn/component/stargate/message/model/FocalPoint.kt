package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a coordinate, or a point of focus, in a two-dimensional plane. This is usually used with images to
 * indicate the important part of the image so that it is still shown when cropping is applied.
 *
 * Note that the value of a coordinate ([x], [y]) can be a float anywhere between -1.0f and +1.0f, where 0.0f is the
 * center. An [x] value of -1.0f indicates the left edge of the image, and an [x] value of 1.0f indicates the right
 * edge. For the y-axis, a [y] value of 1.0f indicates the top edge of the image, and a [y] value of -1.0f indicates
 * the bottom edge.
 *
 * Note that this is useful dependent on the scale type used on a platforms image view. For instance, Android's
 * ImageView component, provides a scaleType property that allows specifying how the image should be scaled and
 * displayed. This allows the image to scaled down to fit in the view bounds. However, we can choose to crop the image
 * using the scaleType property, in this case, using a focal point would be useful so that the cropped image still
 * displays the important part of the image. For this case, we would use the Android ImageView's matrix scaleType and
 * convert this [FocalPoint] to a Matrix.
 *
 * ### JSON Representation Example
 *
 * ```json
 * {
 *     "x": 0.5,
 *     "y": 0.5
 * }
 * ```
 *
 * @property [x] A required and non-null [Float] (32-bit floating point) value that represents the coordinate value on
 * the horizontal axis in the two-dimensional plane. This value must be between -1.0f and 1.0f.
 *
 * @property [y] A required and non-null [Float] (32-bit floating point) value that represents the coordinate value on
 * the vertical axis in the two-dimensional plane. This value must be between -1.0f and 1.0f.
 *
 * @see [jquery-focuspoint](https://github.com/jonom/jquery-focuspoint#1-calculate-your-images-focus-point)
 * @see [Android ImageView ScaleType](https://developer.android.com/reference/android/widget/ImageView.ScaleType)
 */
@Serializable
public data class FocalPoint public constructor(
    @SerialName(value = "x") public val x: Float,
    @SerialName(value = "y") public val y: Float
)
