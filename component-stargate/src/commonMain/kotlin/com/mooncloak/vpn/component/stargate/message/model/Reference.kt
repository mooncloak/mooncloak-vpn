package com.mooncloak.vpn.component.stargate.message.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Reference public constructor(
    @SerialName(value = "relation") public val relation: Relation,
    @SerialName(value = "value") public val value: String
)
