package com.mooncloak.vpn.util.shortcuts

public operator fun AppShortcutProvider.Companion.invoke(): AppShortcutProvider = JvmAppShortcutProvider()

internal class JvmAppShortcutProvider internal constructor() : AppShortcutProvider {

    override suspend fun get(): List<AppShortcut> = emptyList()
}
