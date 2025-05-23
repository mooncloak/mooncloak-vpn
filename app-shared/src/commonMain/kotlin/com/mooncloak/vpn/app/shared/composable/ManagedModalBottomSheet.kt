package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.PartiallyExpanded
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.core.BackHandler
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * An extension on the material3 [ModalBottomSheet] that automatically removes the composable from the UI when it is no
 * longer visible. The material3 [ModalBottomSheet] requires you to keep an additional state, along with the
 * [SheetState], for whether the modal should be displayed. This seems redundant because the [SheetState] already has
 * [SheetState.isVisible] property. Unfortunately, it is required, and if you don't do it, it blocks the interaction
 * with the rest of the UI, even when the [ModalBottomSheet] is no longer visible. So, this component performs that
 * logic internally within the composable, therefore, the user only has to care about the [SheetState].
 *
 * ## Example Usage
 *
 * ```kotlin
 * val bottomSheetState = rememberManagedModalBottomSheet()
 *
 * LaunchedEffect(Unit) {
 *     // Display the bottom sheet after 2 seconds
 *     delay(2.seconds)
 *
 *     bottomSheetState.show()
 * }
 *
 * ManagedModalBottomSheet(
 *     state = bottomSheetState,
 *     content = {
 *         ...
 *     }
 * )
 * ```
 *
 * @see [ModalBottomSheet] for more information on how to use this component.
 */
@Composable
public fun ManagedModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    sheetState: ManagedModalBottomSheetState,
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    minWidth: Dp = 200.dp,
    maxWidth: Dp = 600.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = {
        BottomSheetDefaults.DragHandle(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
        )
    },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(sheetState.isVisible) {
        sheetState.updateAttached(sheetState.isVisible)
    }

    BackHandler(
        enabled = sheetState.isVisible && properties.shouldDismissOnBackPress
    ) {
        coroutineScope.launch {
            sheetState.hide()
        }
    }

    if (sheetState.isAttached) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    onDismissRequest.invoke()
                }
            },
            modifier = Modifier.sizeIn(
                minWidth = minWidth,
                maxWidth = maxWidth
            ).then(modifier),
            sheetState = sheetState.sheetState,
            sheetMaxWidth = sheetMaxWidth,
            shape = shape,
            containerColor = containerColor,
            contentColor = contentColor,
            tonalElevation = tonalElevation,
            scrimColor = scrimColor,
            dragHandle = dragHandle,
            contentWindowInsets = contentWindowInsets,
            properties = properties,
            content = content
        )
    }
}

@Composable
public fun rememberManagedModalBottomSheetState(
    skipPartiallyExpanded: Boolean = false,
    confirmValueChange: (SheetValue) -> Boolean = { true },
): ManagedModalBottomSheetState {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded,
        confirmValueChange = confirmValueChange
    )

    return remember(bottomSheetState) {
        ManagedModalBottomSheetState(
            sheetState = bottomSheetState
        )
    }
}

@Suppress("unused")
@Composable
public fun rememberManagedModalBottomSheetState(
    sheetState: SheetState
): ManagedModalBottomSheetState = remember(sheetState) {
    ManagedModalBottomSheetState(sheetState = sheetState)
}

/**
 * A state handler for the [ManagedModalBottomSheetState]. This component wraps the functionality of the [SheetState]
 * component, but provides extra state for adding and removing a [ModalNavigationBottomSheet] from the UI tree and
 * appropriately coordinates that logic with the show and hide functionality.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
@Stable
@ExperimentalMaterial3Api
public class ManagedModalBottomSheetState internal constructor(
    internal val sheetState: SheetState
) {

    private val mutableIsAttached = mutableStateOf(sheetState.isVisible)

    private val mutex = Mutex(locked = false)

    public val isAttached: Boolean by mutableIsAttached

    internal fun updateAttached(isAttached: Boolean) {
        if (isAttached != mutableIsAttached.value) {
            mutableIsAttached.value = isAttached
        }
    }

    /**
     * The current value of the state.
     *
     * If no swipe or animation is in progress, this corresponds to the state the bottom sheet is
     * currently in. If a swipe or an animation is in progress, this corresponds the state the sheet
     * was in before the swipe or animation started.
     */
    public val currentValue: SheetValue
        get() = sheetState.currentValue

    /**
     * The target value of the bottom sheet state.
     *
     * If a swipe is in progress, this is the value that the sheet would animate to if the swipe
     * finishes. If an animation is running, this is the target value of that animation. Finally, if
     * no swipe or animation is in progress, this is the same as the [currentValue].
     */
    public val targetValue: SheetValue
        get() = sheetState.targetValue

    /** Whether the modal bottom sheet is visible. */
    public val isVisible: Boolean
        get() = sheetState.isVisible

    /**
     * Require the current offset (in pixels) of the bottom sheet.
     *
     * The offset will be initialized during the first measurement phase of the provided sheet
     * content.
     *
     * These are the phases: Composition { -> Effects } -> Layout { Measurement -> Placement } ->
     * Drawing
     *
     * During the first composition, an [IllegalStateException] is thrown. In subsequent
     * compositions, the offset will be derived from the anchors of the previous pass. Always prefer
     * accessing the offset from a LaunchedEffect as it will be scheduled to be executed the next
     * frame, after layout.
     *
     * @throws IllegalStateException If the offset has not been initialized yet
     */
    public fun requireOffset(): Float = sheetState.requireOffset()

    /** Whether the sheet has an expanded state defined. */
    public val hasExpandedState: Boolean
        get() = sheetState.hasExpandedState

    /** Whether the modal bottom sheet has a partially expanded state defined. */
    public val hasPartiallyExpandedState: Boolean
        get() = sheetState.hasPartiallyExpandedState

    /**
     * Fully expand the bottom sheet with animation and suspend until it is fully expanded or
     * animation has been cancelled.
     * *
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    public suspend fun expand() {
        mutex.withLock {
            mutableIsAttached.value = true

            sheetState.expand()
        }
    }

    /**
     * Animate the bottom sheet and suspend until it is partially expanded or animation has been
     * cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     * @throws [IllegalStateException] if [skipPartiallyExpanded] is set to true
     */
    public suspend fun partialExpand() {
        mutex.withLock {
            mutableIsAttached.value = true

            sheetState.partialExpand()
        }
    }

    /**
     * Expand the bottom sheet with animation and suspend until it is [PartiallyExpanded] if defined
     * else [Expanded].
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    public suspend fun show() {
        mutex.withLock {
            mutableIsAttached.value = true

            sheetState.show()
        }
    }

    /**
     * Hide the bottom sheet with animation and suspend until it is fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    public suspend fun hide() {
        mutex.withLock {
            sheetState.hide()

            mutableIsAttached.value = false
        }
    }
}
