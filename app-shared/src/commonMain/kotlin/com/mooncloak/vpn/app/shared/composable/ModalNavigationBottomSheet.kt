package com.mooncloak.vpn.app.shared.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Represents the state for a [ModalNavigationBottomSheet] composable. This essentially wraps a [SheetState] and
 * [NavHostController], which can be accessed via the [sheetState] property, respectively. This
 * also contains other convenience functions for opening and displaying the bottom sheet and navigating to a particular
 * route simultaneously, such as, the [show] functions.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
internal class ModalNavigationBottomSheetState<Destination> internal constructor(
    internal val sheetState: MooncloakModalBottomSheetState,
) {

    internal val destination: State<Destination?>
        get() = mutableDestination

    private val mutableDestination = mutableStateOf<Destination?>(null)

    private val mutex = Mutex(locked = false)

    suspend fun <T : Destination> show(
        destination: T? = null
    ) {
        mutex.withLock {
            mutableDestination.value = destination

            sheetState.show()
        }
    }

    /**
     * Hide the bottom sheet with animation and suspend until it is fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun hide() {
        mutex.withLock {
            sheetState.hide()

            mutableDestination.value = null
        }
    }
}

@Composable
internal fun <Destination> rememberModalNavigationBottomSheetState(
    sheetState: MooncloakModalBottomSheetState = rememberMooncloakModalBottomSheetState()
): ModalNavigationBottomSheetState<Destination> = remember {
    ModalNavigationBottomSheetState(
        sheetState = sheetState
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
internal fun <Destination> ModalNavigationBottomSheet(
    state: ModalNavigationBottomSheetState<Destination>,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = {
        BottomSheetDefaults.DragHandle(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
        )
    },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    builder: @Composable (destination: Destination) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

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
            AnimatedVisibility(
                visible = state.destination.value != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                state.destination.value?.let { destination ->
                    builder.invoke(destination)
                }
            }
        }
    )
}
