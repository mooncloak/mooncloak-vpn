package com.mooncloak.vpn.app.shared.feature.main.composable

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
internal fun MooncloakNavigationScaffold(
    navigationItems: MooncloakNavigationScaffoldState.() -> Unit,
    modifier: Modifier = Modifier,
    navigationColors: NavigationSuiteColors = NavigationSuiteDefaults.colors(),
    containerColor: Color = NavigationSuiteScaffoldDefaults.containerColor,
    contentColor: Color = NavigationSuiteScaffoldDefaults.contentColor,
    navigationWindowInsets: WindowInsets = NavigationRailDefaults.windowInsets,
    bottomAppBarElevation: Dp = NavigationBarDefaults.Elevation,
    bottomAppBarContentPadding: PaddingValues = BottomAppBarDefaults.ContentPadding,
    navigationRailHeader: @Composable (ColumnScope.() -> Unit)? = null,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val windowSizeClass = calculateWindowSizeClass()

    val scope = rememberStateOfItems(navigationItems)

    // Define defaultItemColors here since we can't set NavigationSuiteDefaults.itemColors() as a
    // default for the colors param of the NavigationSuiteScope.item non-composable function.
    val defaultItemColors = NavigationSuiteDefaults.itemColors()

    val navigationSize = remember { mutableStateOf(PaddingValues()) }

    val density = LocalDensity.current

    if (windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact) {
        // We are in a larger screen mode. So use a navigation rail component.
        Row(modifier = modifier) {
            NavigationRail(
                modifier = Modifier.fillMaxHeight()
                    .onSizeChanged { size ->
                        navigationSize.value = PaddingValues(
                            start = density.run { size.width.toDp() }
                        )
                    }, // TODO: Consider offset?
                header = navigationRailHeader,
                containerColor = navigationColors.navigationRailContainerColor,
                contentColor = navigationColors.navigationRailContentColor,
                windowInsets = navigationWindowInsets
            ) {
                scope.value.itemList.forEach {
                    NavigationRailItem(
                        modifier = it.modifier,
                        selected = it.selected,
                        onClick = it.onClick,
                        icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                        enabled = it.enabled,
                        label = it.label,
                        alwaysShowLabel = it.alwaysShowLabel,
                        colors = it.colors?.navigationRailItemColors
                            ?: defaultItemColors.navigationRailItemColors,
                        interactionSource = it.interactionSource
                    )
                }

                if (floatingActionButton != null) {
                    Box(
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        floatingActionButton.invoke()
                    }
                }
            }

            Surface(
                modifier = Modifier.weight(1f),
                color = containerColor,
                contentColor = contentColor
            ) {
                content.invoke(navigationSize.value)
            }
        }
    } else {
        val fabSize = remember { mutableStateOf(IntSize.Zero) }
        val bottomBarSize = remember { mutableStateOf(IntSize.Zero) }
        val fabPlacement = remember {
            derivedStateOf {
                FabPlacement(
                    isDocked = true,
                    left = (bottomBarSize.value.width - fabSize.value.width) / 2,
                    width = fabSize.value.width,
                    height = fabSize.value.height
                )
            }
        }

        // FIXME: The EnableToEdge function call is adding weird top padding to the top app bars.
        /*
        SystemUi.EnableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = MaterialTheme.colorScheme.surface,
                darkScrim = MaterialTheme.colorScheme.surface
            )
        ) {}*/

        // We are in compact screen mode. So use a bottom navigation component.
        androidx.compose.material.Scaffold(
            modifier = modifier,
            backgroundColor = containerColor,
            contentColor = contentColor,
            bottomBar = {
                MooncloakBottomAppBar(
                    modifier = Modifier.fillMaxWidth()
                        .onSizeChanged { size -> bottomBarSize.value = size },
                    backgroundColor = navigationColors.navigationBarContainerColor,
                    contentColor = navigationColors.navigationBarContentColor,
                    cutoutShape = CircleShape,
                    elevation = bottomAppBarElevation,
                    contentPadding = bottomAppBarContentPadding,
                    fabPlacement = fabPlacement.value
                ) {
                    Row(
                        modifier = Modifier.weight(1f)
                            .padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        scope.value.itemList.subList(
                            fromIndex = 0,
                            toIndex = scope.value.itemList.size / 2
                        ).forEach {
                            NavigationBarItem(
                                modifier = it.modifier,
                                selected = it.selected,
                                onClick = it.onClick,
                                icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                                enabled = it.enabled,
                                label = it.label,
                                alwaysShowLabel = it.alwaysShowLabel,
                                colors = it.colors?.navigationBarItemColors
                                    ?: defaultItemColors.navigationBarItemColors,
                                interactionSource = it.interactionSource
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.weight(1f)
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        scope.value.itemList.subList(
                            fromIndex = scope.value.itemList.size / 2,
                            toIndex = scope.value.itemList.size
                        ).forEach {
                            NavigationBarItem(
                                modifier = it.modifier,
                                selected = it.selected,
                                onClick = it.onClick,
                                icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                                enabled = it.enabled,
                                label = it.label,
                                alwaysShowLabel = it.alwaysShowLabel,
                                colors = it.colors?.navigationBarItemColors
                                    ?: defaultItemColors.navigationBarItemColors,
                                interactionSource = it.interactionSource
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                Box(
                    modifier = Modifier.wrapContentSize()
                        .onSizeChanged { size -> fabSize.value = size }
                ) {
                    floatingActionButton?.invoke()
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            content = content
        )
    }
}

@Composable
private fun rememberStateOfItems(
    content: MooncloakNavigationScaffoldState.() -> Unit
): State<MooncloakNavigationScaffoldScopeImpl> {
    val latestContent = rememberUpdatedState(content)

    return remember {
        derivedStateOf { MooncloakNavigationScaffoldScopeImpl().apply(latestContent.value) }
    }
}

/**
 * The scope associated with the [MooncloakNavigationScaffold].
 */
internal sealed interface MooncloakNavigationScaffoldState {

    /**
     * This function sets the parameters of the default Material navigation item to be used with the
     * Navigation Suite Scaffold. The item is called in [NavigationSuite], according to the
     * current [NavigationSuiteType].
     *
     * For specifics about each item component, see [NavigationBarItem], [NavigationRailItem], and
     * [NavigationDrawerItem].
     *
     * @param selected whether this item is selected
     * @param onClick called when this item is clicked
     * @param icon icon for this item, typically an [Icon]
     * @param modifier the [Modifier] to be applied to this item
     * @param enabled controls the enabled state of this item. When `false`, this component will not
     * respond to user input, and it will appear visually disabled and disabled to accessibility
     * services. Note: as of now, for [NavigationDrawerItem], this is always `true`.
     * @param label the text label for this item
     * @param alwaysShowLabel whether to always show the label for this item. If `false`, the label
     * will only be shown when this item is selected. Note: for [NavigationDrawerItem] this is
     * always `true`
     * @param badge optional badge to show on this item
     * @param colors [NavigationSuiteItemColors] that will be used to resolve the colors used for
     * this item in different states. If null, [NavigationSuiteDefaults.itemColors] will be used.
     * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
     * emitting [Interaction]s for this item. You can use this to change the item's appearance
     * or preview the item in different states. Note that if `null` is provided, interactions will
     * still happen internally.
     */
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        label: @Composable (() -> Unit)? = null,
        alwaysShowLabel: Boolean = true,
        badge: (@Composable () -> Unit)? = null,
        colors: NavigationSuiteItemColors? = null,
        interactionSource: MutableInteractionSource? = null
    )
}

@Composable
private fun NavigationItemIcon(
    icon: @Composable () -> Unit,
    badge: (@Composable () -> Unit)? = null,
) {
    if (badge != null) {
        BadgedBox(badge = { badge.invoke() }) {
            icon()
        }
    } else {
        icon()
    }
}

private data class NavigationItem(
    val selected: Boolean,
    val onClick: () -> Unit,
    val icon: @Composable () -> Unit,
    val modifier: Modifier,
    val enabled: Boolean,
    val label: @Composable (() -> Unit)?,
    val alwaysShowLabel: Boolean,
    val badge: (@Composable () -> Unit)?,
    val colors: NavigationSuiteItemColors?,
    // TODO(conradchen): Make this nullable when material3 1.3.0 is released.
    val interactionSource: MutableInteractionSource
)

private class MooncloakNavigationScaffoldScopeImpl : MooncloakNavigationScaffoldState {

    override fun item(
        selected: Boolean,
        onClick: () -> Unit,
        icon: @Composable () -> Unit,
        modifier: Modifier,
        enabled: Boolean,
        label: @Composable (() -> Unit)?,
        alwaysShowLabel: Boolean,
        badge: (@Composable () -> Unit)?,
        colors: NavigationSuiteItemColors?,
        interactionSource: MutableInteractionSource?
    ) {
        itemList.add(
            NavigationItem(
                selected = selected,
                onClick = onClick,
                icon = icon,
                modifier = modifier,
                enabled = enabled,
                label = label,
                alwaysShowLabel = alwaysShowLabel,
                badge = badge,
                colors = colors,
                // TODO(conradchen): Remove the fallback logic when material3 1.3.0 is released.
                interactionSource = interactionSource ?: MutableInteractionSource()
            )
        )
    }

    val itemList: MutableList<NavigationItem> = mutableListOf()
}
