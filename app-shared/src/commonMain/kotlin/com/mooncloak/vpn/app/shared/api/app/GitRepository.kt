package com.mooncloak.vpn.app.shared.api.app

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a Git repository.
 *
 * @property [id] The unique identifier value for this Git repository.
 *
 * @property [name] The name of the Git repository.
 *
 * @property [fullName] The full name of the Git repository.
 *
 * @property [description] A short description of the Git repository. Defaults to `null`.
 *
 * @property [created] The [Instant] that this Git repository was first created, or `null` if unknown. Defaults to
 * `null`.
 *
 * @property [updated] The [Instant] that this Git repository was last updated, or `null` if unknown. Defaults to
 * `null`.
 *
 * @property [private] Whether this Git repository is considered private. A value of `true` if the Git repository is
 * private, `false` otherwise. Defaults to `false`.
 *
 * @property [fork] Whether this Git repository is a fork of another Git repository. Defaults to `false`.
 *
 * @property [url] The URL [String] of the Git repository. This value may be `null` if the project is not public.
 * Defaults to `null`.
 *
 * @property [cloneUrl] The URL [String] for cloning this Git repository. Defaults to `null`.
 *
 * @property [sshCloneUrl] The URL [String] for cloning this Git repository over SSH. Defaults to `null`.
 *
 * @property [mirrorUrl] The URL [String] of the Git repository that this Git repository mirrors. Defaults to `null`.
 *
 * @property [forks] The amount of forks of this Git repository, or `null` if unknown or unsupported. Defaults to
 * `null`.
 *
 * @property [stars] The amount of stars of this Git repository, or `null` if unknown or unsupported. Defaults to
 * `null`.
 *
 * @property [watchers] The amount of watchers of this Git repository, or `null` if unknown or unsupported. Defaults to
 * `null`.
 *
 * @property [openIssues] The amount of open issues for this Git repository, or `null` if unknown or unsupported.
 * Defaults to `null`.
 *
 * @property [tags] [String] tag values that describe this Git repository. Defaults to an empty [List].
 *
 * @property [owner] The [GitRepositoryOwner] or `null` if unknown. Defaults to `null`.
 */
@Immutable
@Serializable
public data class GitRepository public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "full_name") public val fullName: String,
    @SerialName(value = "description") public val description: String? = null,
    @SerialName(value = "created") public val created: Instant? = null,
    @SerialName(value = "updated") public val updated: Instant? = null,
    @SerialName(value = "private") public val private: Boolean = false,
    @SerialName(value = "fork") public val fork: Boolean = false,
    @SerialName(value = "url") public val url: String? = null,
    @SerialName(value = "clone_url") public val cloneUrl: String? = null,
    @SerialName(value = "ssh_clone_url") public val sshCloneUrl: String? = null,
    @SerialName(value = "mirror_url") public val mirrorUrl: String? = null,
    @SerialName(value = "fork_count") public val forks: Int? = null,
    @SerialName(value = "star_count") public val stars: Int? = null,
    @SerialName(value = "watcher_count") public val watchers: Int? = null,
    @SerialName(value = "open_issue_count") public val openIssues: Int? = null,
    @SerialName(value = "tags") public val tags: List<String> = emptyList(),
    @SerialName(value = "owner") public val owner: GitRepositoryOwner? = null
)
