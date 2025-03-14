package com.mooncloak.vpn.app.shared.feature.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.LocalApplicationComponent
import com.mooncloak.vpn.app.shared.di.LocalPresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.rememberDependency
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.main.MainScreen
import com.mooncloak.vpn.app.shared.feature.onboarding.OnboardingScreen
import com.mooncloak.vpn.app.shared.util.navigation.LocalNavController
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.notification_channel_description_shortcuts
import com.mooncloak.vpn.app.shared.resource.notification_channel_description_vpn
import com.mooncloak.vpn.app.shared.resource.notification_channel_name_shortcuts
import com.mooncloak.vpn.app.shared.resource.notification_channel_name_vpn
import com.mooncloak.vpn.app.shared.theme.MooncloakTheme
import com.mooncloak.vpn.app.shared.theme.ThemePreference
import com.mooncloak.vpn.data.shared.keyvalue.state
import com.mooncloak.vpn.util.notification.NotificationChannelId
import com.mooncloak.vpn.util.notification.NotificationPriority
import org.jetbrains.compose.resources.getString

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
        val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
            FeatureDependencies.createApplicationRootComponent(
                applicationComponent = applicationComponent,
                presentationComponent = presentationComponent,
                navController = navController
            )
        }
        val viewModel = remember { componentDependencies.viewModel }
        val snackbarHostState = remember { SnackbarHostState() }
        val imageLoaderFactory = remember(applicationComponent) { applicationComponent.imageLoaderFactory }
        val preferencesStorage = rememberDependency { preferenceStorage }
        val notificationManager = rememberDependency { notificationManager }

        // Make sure to start the billing manager's scope. This allows it to subscribe to events and handle logic
        // correctly.
        val billingManager = rememberDependency { billingManager }
        val vpnConnectionManager = rememberDependency { vpnConnectionManager }
        DisposableEffect(Unit) {
            billingManager.start()
            vpnConnectionManager.start()

            onDispose {
                billingManager.cancel()
                vpnConnectionManager.cancel()
            }
        }

        val themePreference = preferencesStorage.theme.state(initial = ThemePreference.System)

        LaunchedEffect(Unit) {
            // It is safe to call this numerous times (at least on Android). The Android documentation recommends
            // calling this early in the application, so we call it in the root screen.
            // TODO: Move all these definitions to their own util component.
            notificationManager.registerNotificationChannel(
                id = NotificationChannelId.VPN,
                name = getString(Res.string.notification_channel_name_vpn),
                description = getString(Res.string.notification_channel_description_vpn),
                priority = NotificationPriority.MAX
            )
            notificationManager.registerNotificationChannel(
                id = NotificationChannelId.SHORTCUTS,
                name = getString(Res.string.notification_channel_name_shortcuts),
                description = getString(Res.string.notification_channel_description_shortcuts),
                priority = NotificationPriority.MAX
            )

            viewModel.load()
        }

        LaunchedEffect(imageLoaderFactory) {
            SingletonImageLoader.setSafe(imageLoaderFactory)
        }

        MooncloakTheme(
            themePreference = themePreference.value ?: ThemePreference.System
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
                        modifier = Modifier.fillMaxWidth()
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
                                        MainScreen(
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    composable<RootDestination.SystemAuth> {
                                        // TODO: System Auth layout.
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
