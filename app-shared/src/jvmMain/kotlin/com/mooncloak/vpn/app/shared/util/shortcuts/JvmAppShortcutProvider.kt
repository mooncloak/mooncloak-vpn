package com.mooncloak.vpn.app.shared.util.shortcuts

import com.mooncloak.vpn.util.shortcuts.AppShortcut
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider

public operator fun AppShortcutProvider.Companion.invoke(): AppShortcutProvider = JvmAppShortcutProvider()

internal class JvmAppShortcutProvider internal constructor() : AppShortcutProvider {

    override suspend fun get(): List<AppShortcut> = emptyList()
}
