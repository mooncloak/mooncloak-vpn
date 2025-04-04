package com.mooncloak.vpn.app.shared.feature.main.model

import androidx.compose.runtime.Immutable
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
 * @property [selected] Whether this [destination] is currently selected.
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
    public val selected: Boolean = false,
    public val visible: Boolean = true,
    public val enabled: Boolean = true
)

/**
 * Retrieves the default [MainDestinationStateModel] instances for all the [MainDestination]s.
 *
 * @param [startDestination] The [MainDestination] that is first selected and displayed in the UI.
 *
 * @return A [Set] of [MainDestinationStateModel]s.
 */
public fun MainDestination.Companion.states(startDestination: MainDestination): Set<MainDestinationStateModel> =
    setOf(
        MainDestinationStateModel(
            destination = MainDestination.Home,
            selected = startDestination is MainDestination.Home
        ),
        MainDestinationStateModel(
            destination = MainDestination.Servers,
            selected = startDestination is MainDestination.Servers
        ),
        MainDestinationStateModel(
            destination = MainDestination.CryptoWallet,
            selected = startDestination is MainDestination.CryptoWallet
        ),
        MainDestinationStateModel(
            destination = MainDestination.Settings,
            selected = startDestination is MainDestination.Settings
        )
    )
