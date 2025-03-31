package com.mooncloak.vpn.app.android.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.app.android.api.wireguard.AndroidWireGuardConnectionKeyManager
import com.mooncloak.vpn.app.android.api.wireguard.AndroidWireGuardTunnelManager
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardBackend
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.android.info.AndroidAppClientInfo
import com.mooncloak.vpn.app.android.util.WireGuardVpnContextWrapper
import com.mooncloak.vpn.data.sqlite.SqlDriverFactory
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.data.sqlite.invoke
import com.mooncloak.vpn.network.core.ip.LocalDeviceIPAddressProvider
import com.mooncloak.vpn.network.core.ip.invoke
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.shortcuts.AppShortcutManager
import com.mooncloak.vpn.util.shortcuts.invoke
import com.wireguard.android.backend.GoBackend

@Component
@Singleton
internal abstract class AndroidGalaxyApplicationComponent internal constructor(
    @get:Provides override val applicationContext: ApplicationContext,
    @get:Provides override val applicationCoroutineScope: ApplicationCoroutineScope
) : AndroidApplicationComponent() {

    @Provides
    @Singleton
    internal fun provideAppClientInfo(appClientInfo: AndroidAppClientInfo): AppClientInfo =
        appClientInfo

    @Provides
    @Singleton
    internal fun provideDatabaseDriverFactory(context: ApplicationContext): SqlDriverFactory =
        SqlDriverFactory(context = context, schema = MooncloakDatabase.Schema)

    @Provides
    @Singleton
    internal fun provideLocalNetworkManager(context: ApplicationContext): LocalDeviceIPAddressProvider =
        LocalDeviceIPAddressProvider(context = context)

    @Provides
    @Singleton
    internal fun provideWireGuardConnectionKeyManager(manager: AndroidWireGuardConnectionKeyManager): WireGuardConnectionKeyManager =
        manager

    @Provides
    @Singleton
    internal fun provideWireGuardBackend(context: ApplicationContext): WireGuardBackend =
        GoBackend(WireGuardVpnContextWrapper(context))

    @Provides
    @Singleton
    internal fun provideTunnelManager(manager: AndroidWireGuardTunnelManager): TunnelManager = manager

    @Provides
    @Singleton
    internal fun provideAppShortcutManager(context: ApplicationContext): AppShortcutManager =
        AppShortcutManager(context)
}

internal fun ApplicationComponent.Companion.create(
    applicationContext: ApplicationContext,
    coroutineScope: ApplicationCoroutineScope
): AndroidGalaxyApplicationComponent = AndroidGalaxyApplicationComponent::class.create(
    applicationContext = applicationContext,
    applicationCoroutineScope = coroutineScope
)
