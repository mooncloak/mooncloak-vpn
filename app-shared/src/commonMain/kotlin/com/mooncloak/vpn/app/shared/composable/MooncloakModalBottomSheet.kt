package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * An extension on the material3 [ModalBottomSheet] that automatically removes the composable from the UI when it is no
 * longer visible. The material3 [ModalBottomSheet] requires you to keep an additional state, along with the
 * [SheetState], for whether the modal should be displayed. This seems redundant because the [SheetState] already has
 * [SheetState.isVisible] property. Unfortunately, it is required, and if you don't do it, it blocks the interaction
 * with the rest of the UI, even when the [ModalBottomSheet] is no longer visible. So, this component performs that
 * logic internally within the composable, therefore, the user only has to care about the [SheetState].
 *
 * ## Example usage
 *
 * ```kotlin
 * val bottomSheetState = rememberModalBottomSheet()
 *
 * LaunchedEffect(Unit) {
 *     // Display the bottom sheet after 2 seconds
 *     delay(2.seconds)
 *
 *     bottomSheetState.show()
 * }
 *
 * ModalBottomSheet(
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
internal fun MooncloakModalBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    sheetState: SheetState = rememberModalBottomSheetState(),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = BottomSheetDefaults.ExpandedShape,
    containerColor: Color = BottomSheetDefaults.ContainerColor,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = { BottomSheetDefaults.DragHandle() },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    content: @Composable ColumnScope.() -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val openBottomSheet = remember(sheetState) { mutableStateOf(sheetState.isVisible) }

    LaunchedEffect(sheetState.isVisible) {
        val isVisible = sheetState.isVisible

        if (openBottomSheet.value != isVisible) {
            openBottomSheet.value = isVisible
        }
    }

    if (openBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    openBottomSheet.value = false
                    onDismissRequest.invoke()
                }
            },
            modifier = modifier,
            sheetState = sheetState,
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
