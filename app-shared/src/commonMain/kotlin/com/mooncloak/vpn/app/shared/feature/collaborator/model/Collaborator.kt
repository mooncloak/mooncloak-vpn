package com.mooncloak.vpn.app.shared.feature.collaborator.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class Collaborator public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "contact") public val contact: String? = null, // Email, phone, etc.
    @SerialName(value = "avatar") public val avatarUri: String? = null,
    @SerialName(value = "url") public val url: String? = null,
    @SerialName(value = "commit_count") public val commitCount: Long? = null
)
