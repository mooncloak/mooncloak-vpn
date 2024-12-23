package com.mooncloak.vpn.app.shared.api.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a [Contributor]'s Git details associated with the application.
 *
 * @property [totalCommits] The total commits that the [Contributor] added to all [repositories] associated with the
 * application.
 *
 * @property [repositories] The [List] of [GitRepositoryContributorDetails] that the [Contributor] worked on.
 */
@Immutable
@Serializable
public data class GitContributorDetails public constructor(
    @SerialName(value = "total_commits") public val totalCommits: Int? = null,
    @SerialName(value = "repositories") public val repositories: List<GitRepositoryContributorDetails> = emptyList()
)
