package com.mooncloak.vpn.util.shortcuts

public operator fun AppShortcutProvider.Companion.invoke(): AppShortcutProvider = AppAppShortcutProvider()

internal class AppAppShortcutProvider internal constructor() : AppShortcutProvider {

    override suspend fun get(): List<AppShortcut> = emptyList()
}
