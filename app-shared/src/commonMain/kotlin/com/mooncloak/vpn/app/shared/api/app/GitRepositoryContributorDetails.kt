package com.mooncloak.vpn.app.shared.api.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents details about a Git project that a [Contributor] has contributed to.
 *
 * @property [commits] The amount of commits the [Contributor] has made on the project. This value may be `null` if it
 * is unknown. Defaults to `null`.
 *
 * @property [repository] The [GitRepository] that the [Contributor] contributed to.
 */
@Immutable
@Serializable
public data class GitRepositoryContributorDetails public constructor(
    @SerialName(value = "commits") public val commits: Int? = null,
    @SerialName(value = "repository") public val repository: GitRepository
)
