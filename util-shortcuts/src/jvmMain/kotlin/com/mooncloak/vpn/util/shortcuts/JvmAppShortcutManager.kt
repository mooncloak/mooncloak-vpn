package com.mooncloak.vpn.util.shortcuts

public operator fun AppShortcutManager.Companion.invoke(): AppShortcutManager = JvmAppShortcutManager()

internal class JvmAppShortcutManager internal constructor() : AppShortcutManager {

    override suspend fun get(id: String): AppShortcut? = null

    override suspend fun set(shortcuts: Collection<AppShortcut>) {
    }

    override suspend fun perform(id: String) {
    }

    override suspend fun remove(id: String) {
    }

    override suspend fun clear() {
    }
}
