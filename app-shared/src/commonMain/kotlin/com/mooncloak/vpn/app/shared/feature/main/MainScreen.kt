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
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mooncloak.vpn.api.shared.server.isConnectable
import com.mooncloak.vpn.network.core.vpn.isConnected
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.CryptoWalletScreen
import com.mooncloak.vpn.app.shared.feature.home.HomeScreen
import com.mooncloak.vpn.app.shared.feature.main.composable.MooncloakNavigationScaffold
import com.mooncloak.vpn.app.shared.feature.main.model.selected
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

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
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            unselectedContainerColor = MaterialTheme.colorScheme.surface,
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            selectedBadgeColor = MaterialTheme.colorScheme.error,
            unselectedBadgeColor = MaterialTheme.colorScheme.error
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
                .forEach { navigationItemState ->
                    this.item(
                        selected = navigationItemState.selected(currentDestination),
                        enabled = navigationItemState.enabled,
                        onClick = {
                            viewModel.select(navigationItemState.destination)
                        },
                        icon = {
                            navigationItemState.destination.icon?.let { icon ->
                                Icon(
                                    painter = icon,
                                    contentDescription = navigationItemState.destination.contentDescription
                                )
                            }
                        },
                        label = {
                            Text(text = navigationItemState.destination.title)
                        },
                        badge = if (navigationItemState.badged) {
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
                    startDestination = MainDestination.Home
                ) {
                    // Note: We pass the PaddingValues to the screen composable so that the content can be rendered
                    // behind the navigation controls on the z-axis, but can still have enough padding to scroll past
                    // them so they are visible. Otherwise, the content would look "cut-off" at the navigation
                    // controls. This is the same for all of the same main screens.
                    composable<MainDestination.Home> {
                        HomeScreen(
                            modifier = Modifier.fillMaxSize(),
                            containerPaddingValues = paddingValues,
                            onOpenCryptoWallet = {
                                viewModel.select(MainDestination.CryptoWallet)
                            }
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
                            containerPaddingValues = paddingValues,
                            onOpenSupport = {
                                viewModel.select(MainDestination.Support)
                            }
                        )
                    }
                    composable<MainDestination.CryptoWallet> {
                        CryptoWalletScreen(
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
