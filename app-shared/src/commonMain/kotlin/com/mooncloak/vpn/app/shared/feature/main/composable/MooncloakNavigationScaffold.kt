package com.mooncloak.vpn.app.shared.feature.main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailDefaults
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_name
import com.mooncloak.vpn.app.shared.resource.ic_logo_mooncloak
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
    navigationDrawerHeader: @Composable (ColumnScope.() -> Unit)? = { DrawerHeader() },
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

    when {
        windowSizeClass.widthSizeClass >= WindowWidthSizeClass.Expanded -> LargeScreenLayout(
            scope = scope.value,
            modifier = modifier,
            navigationColors = navigationColors,
            defaultItemColors = defaultItemColors,
            containerColor = containerColor,
            contentColor = contentColor,
            navigationDrawerHeader = navigationDrawerHeader,
            scrollState = rememberScrollState(),
            content = content
        )

        windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact -> MediumScreenLayout(
            modifier = modifier,
            scope = scope.value,
            defaultItemColors = defaultItemColors,
            navigationSize = navigationSize.value,
            onNavigationSizeChanged = { paddingValues ->
                navigationSize.value = paddingValues
            },
            density = density,
            navigationColors = navigationColors,
            containerColor = containerColor,
            contentColor = contentColor,
            navigationWindowInsets = navigationWindowInsets,
            navigationRailHeader = navigationRailHeader,
            floatingActionButton = floatingActionButton,
            content = content
        )

        else -> SmallScreenLayout(
            scope = scope.value,
            defaultItemColors = defaultItemColors,
            modifier = modifier,
            navigationColors = navigationColors,
            containerColor = containerColor,
            contentColor = contentColor,
            bottomAppBarElevation = bottomAppBarElevation,
            bottomAppBarContentPadding = bottomAppBarContentPadding,
            floatingActionButton = floatingActionButton,
            navigationDrawerHeader = navigationDrawerHeader,
            drawerState = rememberDrawerState(DrawerValue.Closed),
            scrollState = rememberScrollState(),
            content = content
        )
    }
}

@Composable
private fun SmallScreenLayout(
    scope: MooncloakNavigationScaffoldScopeImpl,
    defaultItemColors: NavigationSuiteItemColors,
    modifier: Modifier,
    drawerState: DrawerState,
    scrollState: ScrollState,
    navigationColors: NavigationSuiteColors,
    containerColor: Color,
    contentColor: Color,
    bottomAppBarElevation: Dp,
    bottomAppBarContentPadding: PaddingValues,
    floatingActionButton: @Composable (() -> Unit)?,
    navigationDrawerHeader: @Composable (ColumnScope.() -> Unit)?,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
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
    val coroutineScope = rememberCoroutineScope()

    // FIXME: The EnableToEdge function call is adding weird top padding to the top app bars.
    /*
    SystemUi.EnableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = MaterialTheme.colorScheme.surface,
            darkScrim = MaterialTheme.colorScheme.surface
        )
    ) {}*/

    // We are in compact screen mode. So use a bottom navigation component.

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerState = drawerState,
                drawerContainerColor = navigationColors.navigationDrawerContainerColor,
                drawerContentColor = navigationColors.navigationDrawerContentColor
            ) {
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                        .padding(horizontal = DefaultHorizontalPageSpacing)
                ) {
                    if (navigationDrawerHeader != null) {
                        navigationDrawerHeader.invoke(this)

                        Spacer(modifier = Modifier.height(28.dp))
                    }

                    scope.itemList.forEach { item ->
                        NavigationDrawerItem(
                            modifier = Modifier.padding(vertical = 4.dp)
                                .pointerHoverIcon(PointerIcon.Hand)
                                .then(item.modifier),
                            selected = item.selected,
                            onClick = {
                                item.onClick.invoke()

                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            },
                            label = {
                                item.label?.invoke()
                            },
                            icon = item.icon,
                            badge = item.badge,
                            colors = item.colors?.navigationDrawerItemColors
                                ?: defaultItemColors.navigationDrawerItemColors
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        },
        content = {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
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
                            scope.primaryItemList.subList(
                                fromIndex = 0,
                                toIndex = scope.itemList.size / 2
                            ).forEach {
                                NavigationBarItem(
                                    modifier = it.modifier.then(Modifier.pointerHoverIcon(PointerIcon.Hand)),
                                    selected = it.selected,
                                    onClick = it.onClick,
                                    icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                                    enabled = it.enabled,
                                    label = {
                                        if (it.alwaysShowLabel) {
                                            it.label?.invoke()
                                        }
                                    },
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
                            // TODO: This is hardcoded to fit the floating action button in the center
                            scope.primaryItemList.subList(
                                fromIndex = scope.itemList.size / 2,
                                toIndex = scope.itemList.size.coerceAtMost(4)
                            ).forEach {
                                NavigationBarItem(
                                    modifier = it.modifier.then(Modifier.pointerHoverIcon(PointerIcon.Hand)),
                                    selected = it.selected,
                                    onClick = it.onClick,
                                    icon = { NavigationItemIcon(icon = it.icon, badge = it.badge) },
                                    enabled = it.enabled,
                                    label = {
                                        if (it.alwaysShowLabel) {
                                            it.label?.invoke()
                                        }
                                    },
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
                            .pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        floatingActionButton?.invoke()
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                isFloatingActionButtonDocked = true,
                content = content
            )
        }
    )
}

@Composable
private fun MediumScreenLayout(
    scope: MooncloakNavigationScaffoldScopeImpl,
    defaultItemColors: NavigationSuiteItemColors,
    modifier: Modifier,
    navigationColors: NavigationSuiteColors,
    containerColor: Color,
    contentColor: Color,
    navigationWindowInsets: WindowInsets,
    navigationSize: PaddingValues,
    density: Density,
    onNavigationSizeChanged: (PaddingValues) -> Unit,
    navigationRailHeader: @Composable (ColumnScope.() -> Unit)?,
    floatingActionButton: @Composable (() -> Unit)?,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    // We are in a larger screen mode. So use a navigation rail component.
    Row(modifier = modifier) {
        NavigationRail(
            modifier = Modifier.fillMaxHeight()
                .onSizeChanged { size ->
                    onNavigationSizeChanged.invoke(PaddingValues(start = density.run { size.width.toDp() }))
                }, // TODO: Consider offset?
            header = navigationRailHeader,
            containerColor = navigationColors.navigationRailContainerColor,
            contentColor = navigationColors.navigationRailContentColor,
            windowInsets = navigationWindowInsets
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            scope.itemList.forEach {
                NavigationRailItem(
                    modifier = it.modifier.then(Modifier.pointerHoverIcon(PointerIcon.Hand)),
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
                        .pointerHoverIcon(PointerIcon.Hand)
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
            content.invoke(navigationSize)
        }
    }
}

@Composable
private fun LargeScreenLayout(
    scope: MooncloakNavigationScaffoldScopeImpl,
    scrollState: ScrollState = rememberScrollState(),
    modifier: Modifier,
    defaultItemColors: NavigationSuiteItemColors,
    navigationColors: NavigationSuiteColors,
    containerColor: Color,
    contentColor: Color,
    navigationDrawerHeader: @Composable (ColumnScope.() -> Unit)?,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    PermanentNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(240.dp),
                drawerContainerColor = navigationColors.navigationDrawerContainerColor,
                drawerContentColor = navigationColors.navigationDrawerContentColor
            ) {
                Column(
                    modifier = Modifier.verticalScroll(scrollState)
                        .padding(horizontal = DefaultHorizontalPageSpacing)
                ) {
                    Spacer(modifier = Modifier.height(12.dp))

                    if (navigationDrawerHeader != null) {
                        navigationDrawerHeader.invoke(this)

                        Spacer(modifier = Modifier.height(28.dp))
                    }

                    scope.itemList.forEach { item ->
                        NavigationDrawerItem(
                            modifier = Modifier.padding(vertical = 4.dp)
                                .pointerHoverIcon(PointerIcon.Hand)
                                .then(item.modifier),
                            selected = item.selected,
                            onClick = item.onClick,
                            label = {
                                item.label?.invoke()
                            },
                            icon = item.icon,
                            badge = item.badge,
                            colors = item.colors?.navigationDrawerItemColors
                                ?: defaultItemColors.navigationDrawerItemColors
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = containerColor,
                contentColor = contentColor
            ) {
                content.invoke(PaddingValues())
            }
        }
    )
}

@Composable
private fun DrawerHeader(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(4.dp)
            ) {
                Image(
                    modifier = Modifier.matchParentSize(),
                    painter = painterResource(Res.drawable.ic_logo_mooncloak),
                    contentDescription = null
                )
            }

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleMedium
            )
        }
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
        interactionSource: MutableInteractionSource? = null,
        primary: Boolean = true
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
    val interactionSource: MutableInteractionSource,
    val primary: Boolean
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
        interactionSource: MutableInteractionSource?,
        primary: Boolean
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
                interactionSource = interactionSource ?: MutableInteractionSource(),
                primary = primary
            )
        )
    }

    val itemList: MutableList<NavigationItem> = mutableListOf()

    val primaryItemList: List<NavigationItem>
        inline get() = itemList.filter { it.primary }
}
