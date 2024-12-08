package com.mooncloak.vpn.app.shared.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class PlanBreakdown public constructor(
    @SerialName(value = "title") public val title: String? = null,
    @SerialName(value = "description") public val description: String? = null,
    @SerialName(value = "points") public val points: List<PlanBulletPoint> = emptyList()
)
