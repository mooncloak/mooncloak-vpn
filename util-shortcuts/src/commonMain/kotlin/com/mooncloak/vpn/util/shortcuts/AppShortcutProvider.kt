package com.mooncloak.vpn.util.shortcuts

public fun interface AppShortcutProvider {

    public suspend fun get(): List<AppShortcut>

    public companion object
}
