package com.mooncloak.vpn.util.shortcuts

import androidx.compose.ui.graphics.ImageBitmap

/**
 * Represents an application shortcut which is a quick action that can be performed without opening the application.
 * [AppShortcut]s are platform-specific and not all platforms will handle them. For instance, on Android these
 * translate to "dynamic app shortcuts" (via the ShortcutInfo class), whereas, on iOS these translate to the
 * UIApplicationShortcutItem class.
 *
 * @property [id] The unique identifier for this shortcut item.
 *
 * @property [shortLabel] The text that is displayed for the shortcut.
 *
 * @property [longLabel] An optional longer version of the [shortLabel] that might be displayed for the shortcut.
 *
 * @see [AppShortcutManager] for updating the shortcuts for the application.
 */
public expect class AppShortcut {

    public val id: String
    public val shortLabel: String
    public val longLabel: String?
}
