package com.mooncloak.vpn.app.shared.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.*
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Listens to changes in the provided [lazyListState] and invokes the [onLoadMore] function
 * appropriately, when the list scrolled to the position of the total item count minus the provided
 * [loadOffset].
 */
@Composable
public fun LaunchLazyLoader(
    lazyListState: LazyListState,
    loadOffset: Int = 5,
    snapshotDispatcher: CoroutineDispatcher = Dispatchers.Main,
    loadDispatcher: CoroutineDispatcher = Dispatchers.PlatformIO,
    enabled: Boolean = true,
    loadOnStart: Boolean = false,
    mutex: Mutex = remember { Mutex(locked = false) },
    onLoadMore: suspend () -> Unit
) {
    // We put this calculation in remember/derivedStateOf function calls to provide better performance. This is
    // because it will only cause a recomposition when the derived value actually changes, not just when the list
    // state value changes (which is very often during scrolling).
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItemIndex = lazyListState.lastVisibleItemIndex ?: 0
            val totalItemsCount = lazyListState.layoutInfo.totalItemsCount

            enabled && (totalItemsCount > 0) && (lastVisibleItemIndex >= totalItemsCount - loadOffset)
        }
    }

    // Create a snapshot flow that listens to the derived state property 'shouldLoadMore' and properly invokes the
    // 'onLoadMore' function when that value changes.
    LaunchedEffect(lazyListState, loadOffset) {
        snapshotFlow { shouldLoadMore.value }
            .flowOn(snapshotDispatcher)
            .distinctUntilChanged()
            .onEach { loadMore ->
                if (loadMore) {
                    mutex.withLock {
                        onLoadMore()
                    }
                }
            }
            .flowOn(loadDispatcher)
            .launchIn(this)
    }

    LaunchedEffect(loadOnStart) {
        withContext(loadDispatcher) {
            if (loadOnStart) {
                mutex.withLock {
                    onLoadMore()
                }
            }
        }
    }
}

/**
 * Listens to changes in the provided [lazyGridState] and invokes the [onLoadMore] function
 * appropriately, when the list scrolled to the position of the total item count minus the provided
 * [loadOffset].
 */
@Composable
public fun LaunchLazyLoader(
    lazyGridState: LazyGridState,
    loadOffset: Int = 5,
    snapshotDispatcher: CoroutineDispatcher = Dispatchers.Main,
    loadDispatcher: CoroutineDispatcher = Dispatchers.PlatformIO,
    enabled: Boolean = true,
    loadOnStart: Boolean = false,
    mutex: Mutex = remember { Mutex(locked = false) },
    onLoadMore: suspend () -> Unit
) {
    // We put this calculation in remember/derivedStateOf function calls to provide better performance. This is
    // because it will only cause a recomposition when the derived value actually changes, not just when the list
    // state value changes (which is very often during scrolling).
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItemIndex = lazyGridState.lastVisibleItemIndex ?: 0
            val totalItemsCount = lazyGridState.layoutInfo.totalItemsCount

            enabled && (totalItemsCount > 0) && (lastVisibleItemIndex >= totalItemsCount - loadOffset)
        }
    }

    // Create a snapshot flow that listens to the derived state property 'shouldLoadMore' and properly invokes the
    // 'onLoadMore' function when that value changes.
    LaunchedEffect(lazyGridState, loadOffset) {
        snapshotFlow { shouldLoadMore.value }
            .flowOn(snapshotDispatcher)
            .distinctUntilChanged()
            .onEach { loadMore ->
                if (loadMore) {
                    mutex.withLock {
                        onLoadMore()
                    }
                }
            }
            .flowOn(loadDispatcher)
            .launchIn(this)
    }

    LaunchedEffect(loadOnStart) {
        withContext(loadDispatcher) {
            if (loadOnStart) {
                mutex.withLock {
                    onLoadMore()
                }
            }
        }
    }
}

/**
 * Listens to changes in the provided [lazyGridState] and invokes the [onLoadMore] function
 * appropriately, when the list scrolled to the position of the total item count minus the provided
 * [loadOffset].
 */
@Composable
public fun LaunchLazyLoader(
    lazyGridState: LazyStaggeredGridState,
    loadOffset: Int = 5,
    snapshotDispatcher: CoroutineDispatcher = Dispatchers.Main,
    loadDispatcher: CoroutineDispatcher = Dispatchers.PlatformIO,
    enabled: Boolean = true,
    loadOnStart: Boolean = false,
    mutex: Mutex = remember { Mutex(locked = false) },
    onLoadMore: suspend () -> Unit
) {
    // We put this calculation in remember/derivedStateOf function calls to provide better performance. This is
    // because it will only cause a recomposition when the derived value actually changes, not just when the list
    // state value changes (which is very often during scrolling).
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItemIndex = lazyGridState.lastVisibleItemIndex ?: 0
            val totalItemsCount = lazyGridState.layoutInfo.totalItemsCount

            enabled && (totalItemsCount > 0) && (lastVisibleItemIndex >= totalItemsCount - loadOffset)
        }
    }

    // Create a snapshot flow that listens to the derived state property 'shouldLoadMore' and properly invokes the
    // 'onLoadMore' function when that value changes.
    LaunchedEffect(lazyGridState, loadOffset) {
        snapshotFlow { shouldLoadMore.value }
            .flowOn(snapshotDispatcher)
            .distinctUntilChanged()
            .onEach { loadMore ->
                if (loadMore) {
                    mutex.withLock {
                        onLoadMore()
                    }
                }
            }
            .flowOn(loadDispatcher)
            .launchIn(this)
    }

    LaunchedEffect(loadOnStart) {
        withContext(loadDispatcher) {
            if (loadOnStart) {
                mutex.withLock {
                    onLoadMore()
                }
            }
        }
    }
}
