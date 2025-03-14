package com.mooncloak.vpn.app.shared.window

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Composable
public actual fun rememberWindowTitleBarState(value: WindowTitleBarStateModel): WindowTitleBarStateHolder =
    remember(value) {
        IosWindowTitleBarState(
            initialValue = value
        )
    }

internal class IosWindowTitleBarState internal constructor(
    initialValue: WindowTitleBarStateModel
) : WindowTitleBarStateHolder {

    override val titleBar: State<WindowTitleBarStateModel>
        get() = mutableTitleBar

    private val mutableTitleBar = mutableStateOf(initialValue)

    private val mutex = Mutex(locked = false)

    override suspend fun updateTitleBar(value: WindowTitleBarStateModel) {
        mutex.withLock {
            mutableTitleBar.value = value
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IosWindowTitleBarState) return false

        if (titleBar != other.titleBar) return false
        if (mutableTitleBar != other.mutableTitleBar) return false

        return mutex == other.mutex
    }

    override fun hashCode(): Int {
        var result = titleBar.hashCode()
        result = 31 * result + mutableTitleBar.hashCode()
        result = 31 * result + mutex.hashCode()
        return result
    }

    override fun toString(): String =
        "IosWindowTitleBarState(mutableTitle=$mutableTitleBar, mutex=$mutex, title=$titleBar)"
}
