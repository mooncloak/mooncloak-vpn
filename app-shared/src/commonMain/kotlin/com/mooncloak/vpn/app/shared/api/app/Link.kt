package com.mooncloak.vpn.app.shared.api.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a link, or URI, and information about the link.
 *
 * @property [uri] The URI [String].
 *
 * @property [title] An optional title value that should be displayed in the UI, instead of the [uri] value. Defaults
 * to `null`.
 *
 * @property [description] An optional description value, that should be displayed in the UI, that provides extra
 * context about the link. Defaults to `null`.
 *
 * @property [icon] An optional URI [String] pointing to an icon to display for this link in the UI. Defaults to
 * `null`.
 */
@Immutable
@Serializable
public data class Link public constructor(
    @SerialName(value = "uri") public val uri: String,
    @SerialName(value = "title") public val title: String? = null,
    @SerialName(value = "description") public val description: String? = null,
    @SerialName(value = "icon") public val icon: String? = null
)
