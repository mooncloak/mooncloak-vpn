package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mooncloak.kodetools.compose.serialization.ColorAsArgbObjectSerializer
import com.mooncloak.kodetools.compose.serialization.ColorAsRgbaObjectSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class PlanBulletPoint public constructor(
    @SerialName(value = "text") public val text: String,
    @SerialName(value = "icon") public val icon: String? = null,
    @SerialName(value = "type") public val type: PlanBulletPointType,
    @SerialName(value = "content_color") @Serializable(with = ColorAsRgbaObjectSerializer::class) public val contentColor: Color? = null
)

@Immutable
@Serializable
@JvmInline
public value class PlanBulletPointType public constructor(
    public val value: String
) {

    public companion object {

        public val Default: PlanBulletPointType = PlanBulletPointType(value = "default")
        public val Positive: PlanBulletPointType = PlanBulletPointType(value = "positive") // Ex: display in green
        public val Negative: PlanBulletPointType = PlanBulletPointType(value = "negative") // Ex: display in red
        public val Neutral: PlanBulletPointType = PlanBulletPointType(value = "neutral") // Ex: display in white
    }
}
