package com.mooncloak.vpn.app.shared.feature.collaborator.api

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
internal data class GithubCommit internal constructor(
    @SerialName(value = "url") val url: String,
    @SerialName(value = "sha") val sha: String? = null,
    @SerialName(value = "commit") val commit: GithubCommitDetails,
    @SerialName(value = "author") val author: GithubCollaborator? = null
)

@Immutable
@Serializable
internal data class GithubCommitDetails internal constructor(
    @SerialName(value = "url") val url: String,
    @SerialName(value = "author") val author: GithubCommitAuthor? = null,
    @SerialName(value = "committer") val committer: GithubCommitAuthor? = null
)

@Immutable
@Serializable
internal data class GithubCommitAuthor internal constructor(
    @SerialName(value = "name") val name: String,
    @SerialName(value = "email") val email: String? = null,
    @SerialName(value = "date") val date: String? = null
)
