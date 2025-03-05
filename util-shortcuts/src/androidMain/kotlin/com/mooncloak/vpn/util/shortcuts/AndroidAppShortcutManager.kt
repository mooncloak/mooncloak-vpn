package com.mooncloak.vpn.util.shortcuts

import android.content.Context
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public operator fun AppShortcutManager.Companion.invoke(
    context: Context
): AppShortcutManager = AndroidAppShortcutManager(
    context = context
)

internal class AndroidAppShortcutManager internal constructor(
    private val context: Context
) : AppShortcutManager {

    private val shortcutsById = mutableMapOf<String, AppShortcut>()

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): AppShortcut? =
        shortcutsById[id]

    override suspend fun set(shortcuts: Collection<AppShortcut>) {
        mutex.withLock {
            shortcutsById.clear()

            val androidShortcuts = shortcuts.distinctBy { it.id }
                .map { it.toShortcutInfo() }

            ShortcutManagerCompat.setDynamicShortcuts(context, androidShortcuts)

            shortcuts.forEach { shortcut ->
                shortcutsById[shortcut.id] = shortcut
            }
        }
    }

    override suspend fun remove(id: String) {
        mutex.withLock {
            ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(id))

            shortcutsById.remove(id)
        }
    }

    override suspend fun clear() {
        mutex.withLock {
            ShortcutManagerCompat.removeAllDynamicShortcuts(context)

            shortcutsById.clear()
        }
    }

    private fun AppShortcut.toShortcutInfo(): ShortcutInfoCompat {
        var builder = ShortcutInfoCompat.Builder(context, this.id)
            .setShortLabel(this.shortLabel)
            .setIntent(this.intent)

        if (!this.longLabel.isNullOrBlank()) {
            builder = builder.setLongLabel(this.longLabel)
        }

        if (this.icon != null) {
            builder = builder.setIcon(this.icon)
        }

        return builder.build()
    }
}
