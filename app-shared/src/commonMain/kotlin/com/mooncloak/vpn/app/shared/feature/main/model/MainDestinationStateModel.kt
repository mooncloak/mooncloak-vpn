package com.mooncloak.vpn.app.shared.feature.main.model

import androidx.compose.runtime.Immutable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mooncloak.vpn.app.shared.feature.main.MainDestination
import kotlinx.serialization.Serializable

/**
 * Represents a state model for the [MainDestination] class. This encapsulates a [MainDestination] instance and state
 * values associated with it for representing it in the UI.
 *
 * @property [destination] The [MainDestination] item.
 *
 * @property [badged] Whether the UI should display a badge for this [destination] item.
 *
 * @property [visible] Whether this [destination] is currently visible.
 *
 * @property [enabled] Whether this [destination] can be selected.
 */
@Serializable
@Immutable
public data class MainDestinationStateModel public constructor(
    public val destination: MainDestination,
    public val badged: Boolean = false,
    public val visible: Boolean = true,
    public val enabled: Boolean = true
)

/**
 * Determines whether this [MainDestinationStateModel.destination] is currently selected.
 */
public inline fun MainDestinationStateModel.selected(currentDestination: NavDestination?): Boolean =
    currentDestination?.hierarchy?.any { it.hasRoute(this.destination::class) } == true

/**
 * Retrieves the default [MainDestinationStateModel] instances for all the [MainDestination]s.
 *
 * @return A [Set] of [MainDestinationStateModel]s.
 */
public fun MainDestination.Companion.states(): Set<MainDestinationStateModel> =
    setOf(
        MainDestinationStateModel(
            destination = MainDestination.Home
        ),
        MainDestinationStateModel(
            destination = MainDestination.Servers
        ),
        MainDestinationStateModel(
            destination = MainDestination.CryptoWallet
        ),
        MainDestinationStateModel(
            destination = MainDestination.Settings
        ),
        MainDestinationStateModel(
            destination = MainDestination.Support
        )
    )
