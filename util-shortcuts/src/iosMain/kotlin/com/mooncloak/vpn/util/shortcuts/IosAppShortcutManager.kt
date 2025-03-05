package com.mooncloak.vpn.util.shortcuts

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationShortcutIcon
import platform.UIKit.UIApplicationShortcutItem
import platform.UIKit.UIMutableApplicationShortcutItem
import platform.UIKit.shortcutItems

public operator fun AppShortcutManager.Companion.invoke(): AppShortcutManager = IosAppShortcutManager()

internal class IosAppShortcutManager internal constructor() : AppShortcutManager {

    private val shortcutsById = mutableMapOf<String, AppShortcut>()

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): AppShortcut? =
        shortcutsById[id]

    override suspend fun set(shortcuts: Collection<AppShortcut>) {
        mutex.withLock {
            shortcutsById.clear()

            val iosShortcuts = shortcuts.distinctBy { it.id }
                .map { it.toShortcutItem() }

            UIApplication.sharedApplication.shortcutItems = iosShortcuts

            shortcuts.forEach { shortcut ->
                shortcutsById[shortcut.id] = shortcut
            }
        }
    }

    override suspend fun remove(id: String) {
        mutex.withLock {
            shortcutsById.remove(id)

            val iosShortcuts = shortcutsById.values
                .map { it.toShortcutItem() }

            UIApplication.sharedApplication.shortcutItems = iosShortcuts
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            UIApplication.sharedApplication.shortcutItems = emptyList<UIApplicationShortcutItem>()

            shortcutsById.clear()
        }
    }

    private fun AppShortcut.toShortcutItem(): UIMutableApplicationShortcutItem =
        UIMutableApplicationShortcutItem(
            type = this.id,
            localizedTitle = this.shortLabel,
            localizedSubtitle = this.longLabel,
            icon = this.icon?.toShortcutIcon(),
            userInfo = null
        )

    // TODO: Implement conversion from Compose Multiplatform ImageBitmap to iOS UIApplicationShortcutIcon
    private fun ImageBitmap.toShortcutIcon(): UIApplicationShortcutIcon? = null
}
