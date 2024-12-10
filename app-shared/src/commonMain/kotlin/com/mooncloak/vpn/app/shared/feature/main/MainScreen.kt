package com.mooncloak.vpn.app.shared.feature.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mooncloak.vpn.app.shared.app.MainDestination
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.country.CountryListScreen
import com.mooncloak.vpn.app.shared.feature.home.HomeScreen
import com.mooncloak.vpn.app.shared.feature.main.di.createMainComponent
import com.mooncloak.vpn.app.shared.feature.settings.SettingsScreen
import com.mooncloak.vpn.app.shared.feature.support.SupportScreen

@Composable
public fun MainScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val componentDependencies = rememberFeatureDependencies {
        FeatureDependencies.createMainComponent(
            applicationDependencies = this,
            navController = navController
        )
    }
    val viewModel = remember { componentDependencies.viewModel }

    val itemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        )
    )

    NavigationSuiteScaffold(
        modifier = modifier,
        navigationSuiteItems = {
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
                                    contentDescription = null
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
        content = {
            NavHost(
                navController = navController,
                startDestination = viewModel.state.current.value.startDestination
            ) {
                composable<MainDestination.Home> {
                    HomeScreen(modifier = Modifier.fillMaxSize())
                }
                composable<MainDestination.Countries> {
                    CountryListScreen(modifier = Modifier.fillMaxSize())
                }
                composable<MainDestination.Support> {
                    SupportScreen(modifier = Modifier.fillMaxSize())
                }
                composable<MainDestination.Settings> {
                    SettingsScreen(modifier = Modifier.fillMaxSize())
                }
            }
        }
    )
}
