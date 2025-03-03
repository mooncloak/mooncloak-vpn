package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VpnLock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_name
import com.mooncloak.vpn.app.shared.resource.cd_destination_home
import com.mooncloak.vpn.app.shared.resource.cd_destination_servers
import com.mooncloak.vpn.app.shared.resource.cd_destination_settings
import com.mooncloak.vpn.app.shared.resource.cd_destination_support
import com.mooncloak.vpn.app.shared.resource.destination_main_home_title
import com.mooncloak.vpn.app.shared.resource.destination_main_servers_title
import com.mooncloak.vpn.app.shared.resource.destination_main_settings_title
import com.mooncloak.vpn.app.shared.resource.destination_main_support_title
import com.mooncloak.vpn.app.shared.resource.destination_onboarding_landing_title
import com.mooncloak.vpn.app.shared.resource.destination_onboarding_tutorial_title
import com.mooncloak.vpn.app.shared.resource.destination_root_onboarding_auth_title
import com.mooncloak.vpn.app.shared.resource.destination_root_onboarding_title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

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
public sealed interface AppDestination {

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

@Immutable
@Serializable
public sealed interface RootDestination : AppDestination {

    @Immutable
    @Serializable
    @SerialName(value = "auth")
    public data object SystemAuth : RootDestination {

        override val path: String = "/auth"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_root_onboarding_auth_title)

        override val icon: Painter?
            @Composable
            get() = null
    }

    @Immutable
    @Serializable
    @SerialName(value = "onboarding")
    public data object Onboarding : RootDestination {

        override val path: String = "/onboarding"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_root_onboarding_title)

        override val icon: Painter?
            @Composable
            get() = null
    }

    @Immutable
    @Serializable
    @SerialName(value = "main")
    public data object Main : RootDestination {

        override val path: String = "/"

        override val title: String
            @Composable
            get() = stringResource(Res.string.app_name)

        override val icon: Painter?
            @Composable
            get() = null
    }
}

@Immutable
@Serializable
public sealed interface OnboardingDestination : AppDestination {

    @Immutable
    @Serializable
    @SerialName(value = "landing")
    public data object Landing : OnboardingDestination {

        override val path: String = "/onboarding/landing"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_onboarding_landing_title)

        override val icon: Painter?
            @Composable
            get() = null
    }

    @Immutable
    @Serializable
    @SerialName(value = "tutorial")
    public data object Tutorial : OnboardingDestination {

        override val path: String = "/onboarding/tutorial"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_onboarding_tutorial_title)

        override val icon: Painter?
            @Composable
            get() = null
    }
}

@Immutable
@Serializable
public sealed interface MainDestination : AppDestination {

    @Serializable
    @SerialName(value = "home")
    @Immutable
    public data object Home : MainDestination {

        override val path: String = "/home"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_home_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.Home)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_home)
    }

    @Serializable
    @SerialName(value = "servers")
    @Immutable
    public data object Servers : MainDestination {

        override val path: String = "/servers"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_servers_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.VpnLock)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_servers)
    }

    @Serializable
    @SerialName(value = "support")
    @Immutable
    public data object Support : MainDestination {

        override val path: String = "/support"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_support_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.SupportAgent)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_support)
    }

    @Serializable
    @SerialName(value = "settings")
    @Immutable
    public data object Settings : MainDestination {

        override val path: String = "/settings"

        override val title: String
            @Composable
            get() = stringResource(Res.string.destination_main_settings_title)

        override val icon: Painter
            @Composable
            get() = rememberVectorPainter(Icons.Default.Settings)

        override val contentDescription: String
            @Composable
            get() = stringResource(Res.string.cd_destination_settings)
    }
}
