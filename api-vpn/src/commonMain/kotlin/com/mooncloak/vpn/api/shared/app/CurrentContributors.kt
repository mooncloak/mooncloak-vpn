package com.mooncloak.vpn.api.shared.app

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents information about the current [Contributor]s to the development of this application.
 *
 * @property [contributors] The [List] of [Contributor]s who worked on the development of this application. Defaults to
 * an empty [List].
 */
@Immutable
@Serializable
public data class CurrentContributors public constructor(
    @SerialName(value = "contributors") public val contributors: List<Contributor> = emptyList()
)
