package com.mooncloak.vpn.app.shared.window

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Composable
public actual fun rememberWindowTitleBarState(value: WindowTitleBarStateModel): WindowTitleBarStateHolder {
    val context = LocalContext.current

    return remember(value) {
        AndroidWindowTitleBarState(
            initialValue = value,
            context = context
        )
    }
}

internal class AndroidWindowTitleBarState internal constructor(
    initialValue: WindowTitleBarStateModel,
    private val context: Context
) : WindowTitleBarStateHolder {

    override val titleBar: State<WindowTitleBarStateModel>
        get() = mutableTitleBar

    private val mutableTitleBar = mutableStateOf(initialValue)

    private val mutex = Mutex(locked = false)

    init {
        (context as? Activity)?.title = initialValue.title
    }

    override suspend fun updateTitleBar(value: WindowTitleBarStateModel) {
        mutex.withLock {
            (context as? Activity)?.title = value.title
            mutableTitleBar.value = value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AndroidWindowTitleBarState) return false

        if (context != other.context) return false
        if (titleBar != other.titleBar) return false

        return mutex == other.mutex
    }

    override fun hashCode(): Int {
        var result = context.hashCode()
        result = 31 * result + titleBar.hashCode()
        result = 31 * result + mutex.hashCode()
        return result
    }

    override fun toString(): String =
        "AndroidWindowTitleBarState(mutex=$mutex, title=$titleBar, context=$context)"
}
