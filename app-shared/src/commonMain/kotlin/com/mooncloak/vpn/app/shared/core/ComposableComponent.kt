package com.mooncloak.vpn.app.shared.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.statex.ViewModel

@Stable
public abstract class ComposableComponent<State : Any> public constructor(
    private val bindOnRemember: Boolean = true
) : RememberObserver {

    public val isBound: androidx.compose.runtime.State<Boolean>
        get() = viewModel.isBound

    public open val key: Any? = this::class.simpleName

    protected abstract val viewModel: ViewModel<State>

    @Composable
    @Suppress("FunctionName")
    protected abstract fun Content()

    @Composable
    public fun Compose(key: Any = this) {
        // For some reason the RememberObserver functions aren't being called. So, this asserts that
        // the bind functions are called.
        DisposableEffect(key) {
            this@ComposableComponent.bind()

            onDispose {
                this@ComposableComponent.unbind()
            }
        }

        if (this@ComposableComponent.isBound.value) {
            Content()
        }
    }

    final override fun onRemembered() {
        if (bindOnRemember) {
            viewModel.onRemembered()
        }
    }

    final override fun onForgotten() {
        if (bindOnRemember) {
            viewModel.onForgotten()
        }
    }

    final override fun onAbandoned() {
        if (bindOnRemember) {
            viewModel.onAbandoned()
        }
    }

    internal fun bind() {
        viewModel.bind()
    }

    internal fun unbind() {
        viewModel.unbind()
    }
}
