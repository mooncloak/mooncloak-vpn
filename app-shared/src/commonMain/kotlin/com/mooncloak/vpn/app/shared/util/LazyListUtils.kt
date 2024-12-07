package com.mooncloak.vpn.app.shared.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf

/**
 * Retrieves the last visible item index value.
 *
 * Note that this property is observable and is updated after every scroll or remeasure. If you use
 * it in the composable function it will be recomposed on every change causing potential
 * performance issues including infinity recomposition loop. Therefore, avoid using it in the
 * composition.
 */
public val LazyListState.lastVisibleItemIndex: Int?
    inline get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index

/**
 * Retrieves the last visible item index value.
 *
 * Note that this property is observable and is updated after every scroll or remeasure. If you use
 * it in the composable function it will be recomposed on every change causing potential
 * performance issues including infinity recomposition loop. Therefore, avoid using it in the
 * composition.
 */
public val LazyGridState.lastVisibleItemIndex: Int?
    inline get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index

/**
 * Retrieves the last visible item index value.
 *
 * Note that this property is observable and is updated after every scroll or remeasure. If you use
 * it in the composable function it will be recomposed on every change causing potential
 * performance issues including infinity recomposition loop. Therefore, avoid using it in the
 * composition.
 */
public val LazyStaggeredGridState.lastVisibleItemIndex: Int?
    inline get() = layoutInfo.visibleItemsInfo.lastOrNull()?.index

/**
 * Retrieves a [State] of the index of the first visible item within the lazy list, or negative one
 * if there are no fully visible items within the lazy list.
 *
 * Note that this property is observable and is updated after every scroll or remeasure. If you use
 * it in the composable function it will be recomposed on every change causing potential
 * performance issues including infinity recomposition loop. Therefore, avoid using it in the
 * composition.
 */
public val LazyListState.firstFullyVisibleItemIndexState: State<Int>
    inline get() = derivedStateOf {
        val layoutInfo = this.layoutInfo

        val firstItem = layoutInfo.visibleItemsInfo.sortedBy { it.index }
            .firstOrNull { itemInfo ->
                itemInfo.offset >= layoutInfo.viewportStartOffset && (itemInfo.offset + itemInfo.size) < layoutInfo.viewportEndOffset
            }

        firstItem?.index ?: -1
    }
