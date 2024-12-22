package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.LocalApplicationComponent
import com.mooncloak.vpn.app.shared.di.LocalPresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.rememberApplicationDependency
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.app.di.createApplicationRootComponent
import com.mooncloak.vpn.app.shared.feature.main.MainScreen
import com.mooncloak.vpn.app.shared.feature.onboarding.OnboardingScreen
import com.mooncloak.vpn.app.shared.navigation.LocalNavController
import com.mooncloak.vpn.app.shared.theme.MooncloakTheme
import com.mooncloak.vpn.app.shared.theme.ThemePreference

@OptIn(ExperimentalPersistentStateAPI::class)
@Composable
public fun ApplicationRootScreen(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    modifier: Modifier = Modifier,
    uriHandler: UriHandler = LocalUriHandler.current
) {
    val navController = rememberNavController()

    CompositionLocalProvider(
        LocalApplicationComponent provides applicationComponent,
        LocalPresentationComponent provides presentationComponent,
        LocalNavController provides navController,
        LocalUriHandler provides uriHandler
    ) {
        val componentDependencies = rememberFeatureDependencies {
            FeatureDependencies.createApplicationRootComponent(
                applicationDependencies = this,
                navController = navController
            )
        }
        val viewModel = remember { componentDependencies.viewModel }
        val imageLoaderFactory = remember(applicationComponent) { applicationComponent.imageLoaderFactory }
        val preferencesStorage = rememberApplicationDependency { preferencesStorage }
        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(Unit) {
            viewModel.load()
        }

        LaunchedEffect(imageLoaderFactory) {
            SingletonImageLoader.setSafe(imageLoaderFactory)
        }

        MooncloakTheme(
            themePreference = preferencesStorage.theme.current.value ?: ThemePreference.System
        ) {
            Scaffold(
                modifier = modifier,
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.sizeIn(
                            maxWidth = 600.dp,
                            maxHeight = 800.dp
                        ).fillMaxWidth()
                    ) {
                        AnimatedVisibility(
                            visible = viewModel.state.current.value.startDestination != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            viewModel.state.current.value.startDestination?.let { startDestination ->
                                NavHost(
                                    navController = navController,
                                    startDestination = startDestination
                                ) {
                                    composable<RootDestination.Onboarding> {
                                        OnboardingScreen(
                                            modifier = Modifier.fillMaxSize(),
                                            onFinish = {
                                                viewModel.finishOnboarding()
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

                    AnimatedVisibility(
                        modifier = Modifier.align(Alignment.Center),
                        visible = viewModel.state.current.value.isLoading
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        LaunchedEffect(viewModel.state.current.value.errorMessage) {
            viewModel.state.current.value.errorMessage?.let { errorMessage ->
                snackbarHostState.showSnackbar(message = errorMessage)
            }
        }
    }
}
