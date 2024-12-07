package com.mooncloak.vpn.app.shared.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.LocalApplicationComponent
import com.mooncloak.vpn.app.shared.feature.LandingLayout
import com.mooncloak.vpn.app.shared.feature.OnboardingLayout
import com.mooncloak.vpn.app.shared.feature.PlansLayout
import com.mooncloak.vpn.app.shared.theme.MooncloakTheme
import com.mooncloak.vpn.app.shared.theme.ThemePreference

@Composable
public fun Application(
    component: ApplicationComponent,
    builder: NavGraphBuilder.() -> Unit
) {
    val controller = rememberNavController()
    val viewModel = remember(component) { component.viewModel }
    val imageLoaderFactory = remember(component) { component.imageLoaderFactory }

    LaunchedEffect(imageLoaderFactory) {
        SingletonImageLoader.setSafe(imageLoaderFactory)
    }

    CompositionLocalProvider(
        LocalApplicationComponent provides component
    ) {
        MooncloakTheme(
            themePreference = ThemePreference.SystemDefault
        ) {
            Surface {
                Column(
                    modifier = Modifier.sizeIn(
                        maxWidth = 600.dp,
                        maxHeight = 800.dp
                    ).fillMaxWidth()
                ) {
                    val isLanding = remember { mutableStateOf(true) }
                    val isOnboarding = remember { mutableStateOf(false) }
                    val isShowingPlans = remember { mutableStateOf(false) }

                    AnimatedVisibility(
                        visible = isLanding.value
                    ) {
                        LandingLayout(
                            modifier = Modifier.fillMaxWidth()
                                .padding(16.dp),
                            onStart = {
                                isLanding.value = false
                                isOnboarding.value = true
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = isOnboarding.value
                    ) {
                        OnboardingLayout(
                            modifier = Modifier.fillMaxWidth()
                                .padding(16.dp),
                            onFinish = {
                                isOnboarding.value = false
                                isShowingPlans.value = true
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = isShowingPlans.value
                    ) {
                        PlansLayout(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}
