package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mooncloak.vpn.api.shared.server.isConnectable
import com.mooncloak.vpn.api.shared.vpn.isConnected
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.feature.app.MainDestination
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.home.HomeScreen
import com.mooncloak.vpn.app.shared.feature.main.composable.MooncloakNavigationScaffold
import com.mooncloak.vpn.app.shared.feature.main.util.containerColor
import com.mooncloak.vpn.app.shared.feature.main.util.contentColor
import com.mooncloak.vpn.app.shared.feature.main.util.floatingActionBarContent
import com.mooncloak.vpn.app.shared.feature.payment.purchase.PaymentScreen
import com.mooncloak.vpn.app.shared.feature.payment.purchase.rememberPurchasingState
import com.mooncloak.vpn.app.shared.feature.server.connection.ServerConnectionScreen
import com.mooncloak.vpn.app.shared.feature.server.connection.rememberServerConnectionBottomSheetState
import com.mooncloak.vpn.app.shared.feature.server.list.ServerListScreen
import com.mooncloak.vpn.app.shared.feature.settings.SettingsScreen
import com.mooncloak.vpn.app.shared.feature.support.SupportScreen
import com.mooncloak.vpn.app.shared.util.navigation.LocalNavController
import kotlinx.coroutines.launch

@Composable
public fun MainScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createMainComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent,
            navController = navController
        )
    }
    val viewModel = remember { componentDependencies.viewModel }

    val paymentPurchasingState = rememberPurchasingState()
    val paymentBottomSheetState = rememberManagedModalBottomSheetState(
        confirmValueChange = { value ->
            when (value) {
                // Disable closing the bottom sheet when we are purchasing to prevent getting in an invalid state.
                SheetValue.Hidden -> !paymentPurchasingState.purchasing.value
                SheetValue.Expanded -> true
                SheetValue.PartiallyExpanded -> true
            }
        }
    )

    val serverConnectionBottomSheetState = rememberServerConnectionBottomSheetState()

    val coroutineScope = rememberCoroutineScope()

    val itemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.primary
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.primary
        )
    )

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    MooncloakNavigationScaffold(
        modifier = modifier,
        navigationItems = {
            viewModel.state.current.value.destinationStates
                .filter { it.visible }
                .forEach { state ->
                    this.item(
                        selected = state.selected,
                        enabled = state.enabled,
                        onClick = {
                            viewModel.select(state.destination)
                        },
                        icon = {
                            state.destination.icon?.let { icon ->
                                Icon(
                                    painter = icon,
                                    contentDescription = state.destination.contentDescription
                                )
                            }
                        },
                        badge = if (state.badged) {
                            @Composable {
                                Badge()
                            }
                        } else {
                            null
                        },
                        alwaysShowLabel = false,
                        colors = itemColors
                    )
                }
        },
        floatingActionButton = {
            val containerColor = animateColorAsState(
                targetValue = viewModel.state.current.value.connection.status.containerColor
            )
            val contentColor = animateColorAsState(
                targetValue = viewModel.state.current.value.connection.status.contentColor
            )

            FloatingActionButton(
                shape = CircleShape,
                containerColor = containerColor.value,
                contentColor = contentColor.value,
                onClick = {
                    coroutineScope.launch {
                        if (
                            viewModel.state.current.value.defaultServer?.isConnectable(hasSubscription = viewModel.state.current.value.subscription != null) == true
                            || viewModel.state.current.value.connection.isConnected()
                        ) {
                            serverConnectionBottomSheetState.show(viewModel.state.current.value.defaultServer)
                        } else {
                            paymentBottomSheetState.show()
                        }
                    }
                },
                content = {
                    viewModel.state.current.value.connection.status.floatingActionBarContent()
                }
            )
        },
        content = { paddingValues ->
            CompositionLocalProvider(LocalNavController provides navController) {
                NavHost(
                    navController = navController,
                    startDestination = viewModel.state.current.value.startDestination
                ) {
                    // Note: We pass the PaddingValues to the screen composable so that the content can be rendered
                    // behind the navigation controls on the z-axis, but can still have enough padding to scroll past
                    // them so they are visible. Otherwise, the content would look "cut-off" at the navigation
                    // controls. This is the same for all of the same main screens.
                    composable<MainDestination.Home> {
                        HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            containerPaddingValues = paddingValues
                        )
                    }
                    composable<MainDestination.Servers> {
                        ServerListScreen(
                            modifier = Modifier.fillMaxSize(),
                            containerPaddingValues = paddingValues,
                            onConnect = { server ->
                                // TODO: Connect to server
                            }
                        )
                    }
                    composable<MainDestination.Support> {
                        SupportScreen(
                            modifier = Modifier.fillMaxSize(),
                            containerPaddingValues = paddingValues
                        )
                    }
                    composable<MainDestination.Settings> {
                        SettingsScreen(
                            modifier = Modifier.fillMaxSize(),
                            containerPaddingValues = paddingValues
                        )
                    }
                }
            }
        }
    )

    PaymentScreen(
        modifier = Modifier.fillMaxWidth(),
        sheetState = paymentBottomSheetState,
        purchasingState = paymentPurchasingState
    )

    ServerConnectionScreen(
        modifier = Modifier.fillMaxWidth(),
        state = serverConnectionBottomSheetState
    )
}
