package com.mooncloak.vpn.component.stargate.entanglement

import androidx.compose.ui.text.AnnotatedString
import com.mooncloak.kodetools.compose.serialization.AnnotatedStringSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Profile public constructor(
    @SerialName(value = "display_name") public val displayName: String? = null,
    @SerialName(value = "description") @Serializable(with = AnnotatedStringSerializer::class) public val description: AnnotatedString? = null,
    @SerialName(value = "images") public val images: ProfileImages? = null,
    @SerialName(value = "links") public val links: List<ProfileLink> = emptyList()
)
