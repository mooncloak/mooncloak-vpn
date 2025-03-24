package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import com.mooncloak.vpn.app.shared.feature.app.AppDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.destination_onboarding_landing_title
import com.mooncloak.vpn.app.shared.resource.destination_onboarding_tutorial_title
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

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
