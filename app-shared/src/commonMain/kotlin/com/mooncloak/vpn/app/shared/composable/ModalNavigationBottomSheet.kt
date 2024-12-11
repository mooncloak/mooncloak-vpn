package com.mooncloak.vpn.app.shared.composable

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.PartiallyExpanded
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mooncloak.vpn.app.shared.navigation.LocalNavController
import kotlinx.coroutines.CancellationException
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * Represents the state for a [ModalNavigationBottomSheet] composable. This essentially wraps a [SheetState] and
 * [NavHostController], which can be accessed via the [sheetState] and [navController] properties, respectively. This
 * also contains other convenience functions for opening and displaying the bottom sheet and navigating to a particular
 * route simultaneously, such as, the [show] functions.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
internal class ModalNavigationBottomSheetState internal constructor(
    internal val sheetState: MooncloakModalBottomSheetState,
    internal val navController: NavHostController
) {

    /**
     * Expand the bottom sheet with animation and suspend until it is [PartiallyExpanded] if defined
     * else [Expanded].
     *
     * @param [route] The destination to show in the bottom sheet. If `null`, then the current destination will
     * be shown.
     *
     * @param [navOptions] The [NavOptions] passed to the [NavHostController].
     *
     * @param [navigatorExtras] The [Navigator.Extras] passed to the [NavHostController].
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun <T : Any> show(
        route: T? = null,
        navOptions: NavOptions? = null,
        navigatorExtras: Navigator.Extras? = null
    ) {
        sheetState.show()

        if (route != null) {
            sheetState.enqueueTask {
                navController.navigate(
                    route = route,
                    navOptions = navOptions,
                    navigatorExtras = navigatorExtras
                )
            }
        }
    }

    /**
     * Expand the bottom sheet with animation and suspend until it is [PartiallyExpanded] if defined
     * else [Expanded].
     *
     * @param [route] The destination to show in the bottom sheet. If `null`, then the current destination will
     * be shown.
     *
     * @param [builder] The [NavOptionsBuilder] passed to the [NavHostController].
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun <T : Any> show(
        route: T? = null,
        builder: NavOptionsBuilder.() -> Unit
    ) {
        sheetState.show()

        if (route != null) {
            sheetState.enqueueTask {
                navController.navigate(
                    route = route,
                    builder = builder
                )
            }
        }
    }

    /**
     * Hide the bottom sheet with animation and suspend until it is fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun hide() {
        sheetState.hide()
    }
}

@Composable
internal fun rememberModalNavigationBottomSheetState(
    sheetState: MooncloakModalBottomSheetState = rememberMooncloakModalBottomSheetState(),
    navController: NavHostController = rememberNavController()
): ModalNavigationBottomSheetState = remember {
    ModalNavigationBottomSheetState(
        sheetState = sheetState,
        navController = navController
    )
}

/**
 * A [MooncloakModalBottomSheet] that uses a [NavHost] for its content. The required [ModalNavigationBottomSheetState]
 * has a [NavHostController] that handles the loading of new routes. This component is provided as a convenience to
 * wrap verbose common functionality.
 *
 * ## Example Usage
 *
 * ```kotlin
 * val state = rememberModalNavigationBottomSheetState(
 *     sheetState = rememberModalBottomSheetState(),
 *     navController = rememberNavController()
 * )
 *
 * ModalNavigationBottomSheet(
 *     startDestination = AppDestination.Home,
 *     state = state
 * ) {
 *     // Construct your navigation graph here, for example:
 *     composable<AppDestination.Home> { ... }
 * }
 * ```
 *
 * @see [MooncloakModalBottomSheet]
 * @see [ModalBottomSheet]
 * @see [ModalNavigationBottomSheetState]
 * @see [SheetState]
 * @see [NavHostController]
 * @see [NavHost]
 */
@Composable
internal fun ModalNavigationBottomSheet(
    startDestination: Any,
    state: ModalNavigationBottomSheetState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    contentAlignment: Alignment = Alignment.TopStart,
    route: KClass<*>? = null,
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    enterTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        {
            fadeIn(animationSpec = tween(700))
        },
    exitTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        {
            fadeOut(animationSpec = tween(700))
        },
    popEnterTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) =
        enterTransition,
    popExitTransition:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) =
        exitTransition,
    sizeTransform:
    (@JvmSuppressWildcards
    AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform?)? =
        null,
    builder: NavGraphBuilder.() -> Unit
) {
    MooncloakModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = state.sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = dragHandle,
        contentWindowInsets = contentWindowInsets,
        properties = properties,
        content = {
            CompositionLocalProvider(LocalNavController provides state.navController) {
                NavHost(
                    navController = state.navController,
                    startDestination = startDestination,
                    contentAlignment = contentAlignment,
                    route = route,
                    typeMap = typeMap,
                    enterTransition = enterTransition,
                    exitTransition = exitTransition,
                    popEnterTransition = popEnterTransition,
                    popExitTransition = popExitTransition,
                    sizeTransform = sizeTransform,
                    builder = builder
                )
            }
        }
    )
}
