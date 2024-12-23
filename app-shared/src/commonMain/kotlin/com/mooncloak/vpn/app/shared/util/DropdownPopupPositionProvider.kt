package com.mooncloak.vpn.app.shared.util

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

public class DropdownPopupPositionProvider public constructor(
    private val verticalOffset: Int = 0
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val isBelowContent = anchorBounds.bottom + popupContentSize.height < windowSize.height

        return IntOffset(
            x = anchorBounds.left,
            y = if (isBelowContent) {
                anchorBounds.bottom + verticalOffset
            } else {
                anchorBounds.top - popupContentSize.height - verticalOffset
            }
        )
    }
}
