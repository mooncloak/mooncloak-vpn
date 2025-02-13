package com.mooncloak.vpn.app.shared.window

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.painter.Painter

/**
 * Represents a state model for a Window's Title Bar.
 *
 * @property [title] The title for the Window. Note that this will not be displayed in the Window's
 * Title Bar if [content] is provided. However, some platforms may still use this value in some of
 * their window manager UIs. Defaults to `null`.
 *
 * @property [icon] The icon [Painter] for the Window. This is typically displayed next to the
 * [title] on platforms that support it. Note that this will not be displayed in the Window's Title
 * Bar if [content] is provided. However, some platforms may still use this value in some of their
 * window manager UIs. Defaults to `null`.
 *
 * @property [content] The [Composable] content to display in the Window's Title Bar next to the
 * Window's controls. Note that if this value is not `null`, then the [title] and [icon] will not
 * be displayed in the Window's Title Bar; only the [content] will be displayed. Defaults to
 * `null`.
 */
@Immutable
public data class WindowTitleBarStateModel public constructor(
    public val title: String? = null,
    public val icon: Painter? = null,
    public val content: (@Composable RowScope.() -> Unit)? = null
)

/**
 * Represents the state for the platform-specific window title component. This allows an
 * application to retrieve the current platform window title (via [titleBar]), or set the current
 * platform window title (via [updateTitleBar]).
 *
 * > [!Note]
 * > This requires opt-in from the platform and out-of-band changes to the title bar may not be
 * > properly reflected in the returned [titleBar] state.
 */
public interface WindowTitleBarStateHolder {

    /**
     * A [State] representing the current [WindowTitleBarStateModel]. This value will change when
     * the [updateTitleBar] function is invoked. However, note that out-of-band changes
     * (ex: platform changes without using [updateTitleBar]) may not be reflected here.
     */
    public val titleBar: State<WindowTitleBarStateModel>

    /**
     * Updates the [WindowTitleBarStateModel] value to be the provided [value].
     *
     * @param [value] The [WindowTitleBarStateModel] value for the window associated with this
     * [WindowTitleBarStateHolder].
     */
    public suspend fun updateTitleBar(value: WindowTitleBarStateModel)

    public companion object
}

/**
 * Updates the [WindowTitleBarStateModel] value to be the provided values. This is a convenience function for
 * constructing an instance of [WindowTitleBarStateModel] and invoking the other
 * [WindowTitleBarStateHolder.updateTitleBar] that takes a [WindowTitleBarStateModel] as a parameter.
 *
 * @see [WindowTitleBarStateHolder.updateTitleBar]
 * @see [WindowTitleBarStateModel]
 */
public suspend inline fun WindowTitleBarStateHolder.updateTitleBar(
    title: String?,
    icon: Painter? = null,
    noinline content: (@Composable RowScope.() -> Unit)? = null
) {
    updateTitleBar(
        value = WindowTitleBarStateModel(
            title = title,
            icon = icon,
            content = content
        )
    )
}

/**
 * Retrieves and remembers a platform-specific [WindowTitleBarStateHolder] instance for the platform's current Window.
 *
 * @param [value] The initial [WindowTitleBarStateModel] for the associated window.
 */
@Composable
public expect fun rememberWindowTitleBarState(value: WindowTitleBarStateModel): WindowTitleBarStateHolder

/**
 * Retrieves and remembers a platform-specific [WindowTitleBarStateHolder] instance for the platform's current Window.
 * This is a convenience function for constructing an instance of [WindowTitleBarStateModel] and invoking the other
 * [rememberWindowTitleBarState] that takes a [WindowTitleBarStateModel] as a parameter.
 *
 * @see [rememberWindowTitleBarState]
 * @see [WindowTitleBarStateModel]
 */
@Composable
public inline fun rememberWindowTitleBarState(
    title: String? = null,
    icon: Painter? = null,
    noinline content: (@Composable RowScope.() -> Unit)? = null
): WindowTitleBarStateHolder = rememberWindowTitleBarState(
    value = WindowTitleBarStateModel(
        title = title,
        icon = icon,
        content = content
    )
)

/**
 * A [ProvidableCompositionLocal] for the [WindowTitleBarStateHolder] component. This should be provided by the
 * component that handles the displaying of content in a platform window.
 */
public val LocalWindowTitleBarState: ProvidableCompositionLocal<WindowTitleBarStateHolder> =
    staticCompositionLocalOf { error("No LocalWindowTitleState provided.") }

/**
 * A [Composable] that allows setting the current platform window's title bar value in a declarative way. This
 * internally obtains the current [WindowTitleBarStateHolder] via the [LocalWindowTitleBarState] property, and invokes
 * the [WindowTitleBarStateHolder.updateTitleBar] function with the provided [title] parameter value.
 *
 * > [!Note]
 * > The last invocation of this [Composable] function will set the title. For example, if there are multiple
 * > [WindowTitleBar] invocations within a [Composable] layout, the last one will be the one that sets the title value
 * > as the other previously set values will be overwritten.
 *
 * > [!Note]
 * > Even though this is a [Composable] function, its UI is out of the current [Composable] UI hierarchy. It is
 * > assigned to the current platform Window component. It is done this way so that each "screen" component can assign
 * > it's own UI to the Window, without each Window having to know about the "screen" content.
 *
 * @param [value] The value to set as the current platform Window's [WindowTitleBarStateModel] value.
 */
@Composable
public fun WindowTitleBar(
    value: WindowTitleBarStateModel
) {
    val windowTitleState = LocalWindowTitleBarState.current

    LaunchedEffect(value) {
        windowTitleState.updateTitleBar(value)
    }
}

/**
 * A [Composable] that allows setting the current platform window's title bar value in a declarative way. This
 * internally obtains the current [WindowTitleBarStateHolder] via the [LocalWindowTitleBarState] property, and invokes
 * the [WindowTitleBarStateHolder.updateTitleBar] function with the provided [title] parameter value.
 *
 * > [!Note]
 * > The last invocation of this [Composable] function will set the title. For example, if there are multiple
 * > [WindowTitleBar] invocations within a [Composable] layout, the last one will be the one that sets the title value
 * > as the other previously set values will be overwritten.
 *
 * > [!Note]
 * > Even though this is a [Composable] function, its UI is out of the current [Composable] UI hierarchy. It is
 * > assigned to the current platform Window component. It is done this way so that each "screen" component can assign
 * > it's own UI to the Window, without each Window having to know about the "screen" content.
 *
 * @see [WindowTitleBar]
 * @see [WindowTitleBarStateModel]
 */
@Composable
public fun WindowTitleBar(
    title: String?,
    icon: Painter? = null,
    content: (@Composable RowScope.() -> Unit)? = null
) {
    val value = remember(title, icon, content) {
        WindowTitleBarStateModel(
            title = title,
            icon = icon,
            content = content
        )
    }
    val windowTitleState = LocalWindowTitleBarState.current

    LaunchedEffect(value) {
        windowTitleState.updateTitleBar(value)
    }
}
