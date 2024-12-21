package com.mooncloak.vpn.app.shared.feature.collaborator.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal data class GithubCollaborator internal constructor(
    @SerialName(value = "login") val login: String,
    @SerialName(value = "id") val id: Long,
    @SerialName(value = "node_id") val nodeId: String? = null,
    @SerialName(value = "avatar_url") val avatarUrl: String? = null,
    @SerialName(value = "gravatar_id") val gravatarId: String? = null,
    @SerialName(value = "url") val url: String? = null,
    @SerialName(value = "type") val type: String? = null,
    @SerialName(value = "role_name") val roleName: String? = null
)
