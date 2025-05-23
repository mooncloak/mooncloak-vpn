package com.mooncloak.vpn.app.shared.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.onboarding.composable.LandingLayout
import com.mooncloak.vpn.app.shared.feature.onboarding.composable.OnboardingLayout
import com.mooncloak.vpn.app.shared.util.navigation.LocalNavController

@Composable
public fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createOnboardingComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent
        )
    }
    val viewModel = remember { componentDependencies.viewModel }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Box(modifier = modifier) {
        CompositionLocalProvider(
            LocalNavController provides navController
        ) {
            NavHost(
                navController = navController,
                startDestination = viewModel.state.current.value.startDestination
            ) {
                composable<OnboardingDestination.Landing> {
                    LandingLayout(
                        modifier = Modifier.fillMaxSize(),
                        version = viewModel.state.current.value.appVersion,
                        onStart = {
                            if (viewModel.state.current.value.viewedOnboarding) {
                                onFinish.invoke()
                            } else {
                                navController.navigate(OnboardingDestination.Tutorial) {
                                    popUpTo(OnboardingDestination.Landing) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    )
                }
                composable<OnboardingDestination.Tutorial> {
                    OnboardingLayout(
                        modifier = Modifier.fillMaxSize(),
                        isGooglePlayBuild = viewModel.state.current.value.isGooglePlayBuild,
                        onFinish = onFinish
                    )
                }
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = viewModel.state.current.value.isLoading
        ) {
            CircularProgressIndicator()
        }
    }
}
