package com.mooncloak.vpn.app.shared.api.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents an owner of a [GitRepository]. This can be a User or an Organization.
 *
 * @property [id] The unique identifier of the owner.
 *
 * @property [name] The name of the owner.
 *
 * @property [avatar] An avatar image URL [String]. Defaults to `null`.
 *
 * @property [url] The URL [String] pointing to the owner, or `null` if unknown. Defaults to `null`.
 *
 * @property [type] The type of the owner, or `null` if unknown. This could be "user", "organization", or any other
 * [String] value.
 */
@Immutable
@Serializable
public data class GitRepositoryOwner public constructor(
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "avatar") public val avatar: String? = null,
    @SerialName(value = "url") public val url: String? = null,
    @SerialName(value = "type") public val type: String? = null
)
