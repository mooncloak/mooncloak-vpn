package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_name
import com.mooncloak.vpn.app.shared.resource.destination_root_onboarding_auth_title
import com.mooncloak.vpn.app.shared.resource.destination_root_onboarding_title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

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
