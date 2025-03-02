package com.mooncloak.vpn.app.shared.feature.server.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.composable.rememberManagedModalBottomSheetState
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Stable
public class ServerDetailsBottomSheetState internal constructor(
    internal val bottomSheetState: ManagedModalBottomSheetState
) {

    public val server: State<Server?>
        get() = mutableServer

    private val mutableServer = mutableStateOf<Server?>(null)

    private val mutex = Mutex(locked = false)

    public suspend fun show(server: Server) {
        mutex.withLock {
            mutableServer.value = server
            bottomSheetState.show()
        }
    }

    public suspend fun hide() {
        mutex.withLock {
            bottomSheetState.hide()
            mutableServer.value = null
        }
    }
}

@Composable
public fun rememberServerDetailsBottomSheetState(
    bottomSheetState: ManagedModalBottomSheetState = rememberManagedModalBottomSheetState()
): ServerDetailsBottomSheetState = remember(bottomSheetState) {
    ServerDetailsBottomSheetState(bottomSheetState = bottomSheetState)
}
