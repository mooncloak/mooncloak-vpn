package com.mooncloak.vpn.app.shared.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a destination within the application. Instances of this interface define different
 * routes that can be opened within the application.
 *
 * > [!Note]
 * > This interface is not sealed because each feature can define their own destinations (which
 * > would not be in the same package which is a requirement for sealed components).
 *
 * > [!Warning]
 * > Each destination should be annotated with [Immutable] and [Serializable] to properly work with
 * > the navigation components.
 */
@Immutable
public interface AppDestination {

    /**
     * A unique URI path value for this destination. This value will be used for deep linking to
     * this destination within the application. This value may be `null` if deep linking to this
     * destination is not supported.
     */
    public val path: String?

    /**
     * A displayable name for this [AppDestination]. This value can be displayed in a navigation
     * component, such as top app bar or bottom nav bar.
     */
    public val title: String
        @Composable get

    /**
     * A displayable [Painter] icon for this [AppDestination]. This value can be displayed in a
     * navigation component, such as a bottom nav bar or navigation rail.
     */
    public val icon: Painter?
        @Composable get

    /**
     * Retrieves the content description for the [icon] for accessibility purposes.
     */
    public val contentDescription: String? get() = null

    @Serializable
    @SerialName(value = "provisioning")
    @Immutable
    public data object Provisioning : AppDestination {

        override val path: String = "/"

        override val title: String
            @Composable
            get() = "Provisioning"

        override val icon: Painter?
            @Composable
            get() = null
    }

    @Serializable
    @SerialName(value = "home")
    @Immutable
    public data object Home : AppDestination {

        override val path: String = "/home"

        override val title: String
            @Composable
            get() = "Home"

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.Home)
    }

    @Serializable
    @SerialName(value = "settings")
    @Immutable
    public data object Settings : AppDestination {

        override val path: String = "/settings"

        override val title: String
            @Composable
            get() = "Settings"

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.Settings)
    }

    public companion object
}

public fun AppDestination.Companion.mainNavigationStates(startDestination: AppDestination): Set<AppDestinationStateModel> =
    setOf(
        AppDestinationStateModel(
            destination = AppDestination.Home,
            isSelected = startDestination is AppDestination.Home
        ),
        AppDestinationStateModel(
            destination = AppDestination.Settings,
            isSelected = startDestination is AppDestination.Settings
        )
    )
