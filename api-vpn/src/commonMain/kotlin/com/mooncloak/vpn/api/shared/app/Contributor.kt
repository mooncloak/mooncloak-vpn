package com.mooncloak.vpn.api.shared.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an individual who contributed to the application, such as a developer of the application.
 *
 * @property [id] The unique identifier for this [Contributor].
 *
 * @property [name] The display name of this [Contributor].
 *
 * @property [position] The name of the position of this [Contributor] within the [company]. For instance, "Software
 * Engineer". Defaults to `null`.
 *
 * @property [company] The name of the company this [Contributor] is a part of. For instance, "mooncloak". Defaults to
 * `null`.
 *
 * @property [contact] The public contact information for this [Contributor]. Defaults to `null`.
 *
 * @property [avatarUri] The URI [String] for an avatar photo of this [Contributor]. Defaults to `null`.
 *
 * @property [self] A URI [String] that points to a detailed website for this [Contributor]. Defaults to `null`.
 *
 * @property [url] A URL [String] belonging to this [Contributor]. For instance, this could be the [Contributor]'s
 * website URL. Defaults to `null`.
 *
 * @property [git] The [GitContributorDetails] or `null` if unknown or doesn't apply. Defaults to `null`.
 *
 * @property [roles] The roles that this [Contributor] has at the [company]. Defaults to an empty [List].
 *
 * @property [groups] The groups that this [Contributor] belongs to. Defaults to an empty [List].
 *
 * @property [links] A [List] of [Link]s that this [Contributor] wishes to showcase. For instance, these can be links
 * to social accounts.
 */
@Immutable
@Serializable
public data class Contributor public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "position") public val position: String? = null,
    @SerialName(value = "company") public val company: String? = null,
    @SerialName(value = "contact") public val contact: String? = null, // Email, phone, etc.
    @SerialName(value = "avatar") public val avatarUri: String? = null,
    @SerialName(value = "self") public val self: String? = null,
    @SerialName(value = "url") public val url: String? = null,
    @SerialName(value = "git") public val git: GitContributorDetails? = null,
    @SerialName(value = "roles") public val roles: List<String> = emptyList(),
    @SerialName(value = "groups") public val groups: List<String> = emptyList(),
    @SerialName(value = "links") public val links: List<Link> = emptyList()
)
