package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.LocalApplicationComponent
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.app.di.createApplicationRootComponent
import com.mooncloak.vpn.app.shared.feature.main.MainScreen
import com.mooncloak.vpn.app.shared.feature.onboarding.OnboardingScreen
import com.mooncloak.vpn.app.shared.navigation.LocalNavController
import com.mooncloak.vpn.app.shared.theme.MooncloakTheme
import com.mooncloak.vpn.app.shared.theme.ThemePreference

@Composable
public fun ApplicationRootScreen(
    component: ApplicationComponent,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalApplicationComponent provides component,
        LocalNavController provides navController
    ) {
        val componentDependencies = rememberFeatureDependencies {
            FeatureDependencies.createApplicationRootComponent(
                applicationDependencies = this,
                navController = navController
            )
        }
        val viewModel = remember { componentDependencies.viewModel }
        val imageLoaderFactory = remember(component) { component.imageLoaderFactory }

        LaunchedEffect(Unit) {
            viewModel.load()
        }

        LaunchedEffect(imageLoaderFactory) {
            SingletonImageLoader.setSafe(imageLoaderFactory)
        }

        MooncloakTheme(
            themePreference = ThemePreference.System
        ) {
            Surface(modifier = modifier) {
                Column(
                    modifier = Modifier.sizeIn(
                        maxWidth = 600.dp,
                        maxHeight = 800.dp
                    ).fillMaxWidth()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = viewModel.state.current.value.startDestination
                    ) {
                        composable<RootDestination.Splash> {
                            // TODO: Application Root Loading Splash Screen UI
                        }
                        composable<RootDestination.Onboarding> {
                            OnboardingScreen(
                                modifier = Modifier.fillMaxSize(),
                                onFinish = {
                                    // TODO: Delegate to ViewModel so that we can store the appropriate values
                                    navController.navigate(RootDestination.Main)
                                }
                            )
                        }
                        composable<RootDestination.Main> {
                            MainScreen(modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            }
        }
    }
}
