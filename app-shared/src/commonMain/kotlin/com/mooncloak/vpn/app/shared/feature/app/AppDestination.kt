package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
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
    public val contentDescription: String? @Composable get() = null

    public companion object
}
