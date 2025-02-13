package com.mooncloak.vpn.app.shared.window

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.WindowScope
import java.awt.MouseInfo
import java.awt.Point
import java.awt.Window

/**
 * WindowDraggableArea is a component that allows you to drag the window using the mouse.
 *
 * > [!Note]
 * > This is adapted from the Compose Multiplatform framework, to allow us to support double click operations as well.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param content The content lambda.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun WindowScope.WindowDraggableArea(
    modifier: Modifier = Modifier,
    onDoubleTap: (offset: Offset) -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val handler = remember { DragHandler(window) }

    Box(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = onDoubleTap
            )
        }.pointerInput(Unit) {
            detectDragGestures(
                matcher = PointerMatcher.Primary,
                onDragStart = {
                    handler.onDragStarted()
                },
                onDrag = {
                    handler.onDrag()
                }
            )
        }
    ) {
        content()
    }
}

/**
 * Converts AWT [Point] to compose [IntOffset]
 */
private fun Point.toComposeOffset() = IntOffset(x = x, y = y)

/**
 * Returns the position of the mouse pointer, in screen coordinates.
 */
private fun currentPointerLocation(): IntOffset? {
    return MouseInfo.getPointerInfo()?.location?.toComposeOffset()
}

private class DragHandler(
    private val window: Window
) {

    private var windowLocationAtDragStart: IntOffset? = null
    private var dragStartPoint: IntOffset? = null

    fun onDragStarted() {
        dragStartPoint = currentPointerLocation() ?: return
        windowLocationAtDragStart = window.location.toComposeOffset()
    }

    fun onDrag() {
        val windowLocationAtDragStart = this.windowLocationAtDragStart ?: return
        val dragStartPoint = this.dragStartPoint ?: return
        val point = currentPointerLocation() ?: return
        val newLocation = windowLocationAtDragStart + (point - dragStartPoint)
        window.setLocation(newLocation.x, newLocation.y)
    }
}
