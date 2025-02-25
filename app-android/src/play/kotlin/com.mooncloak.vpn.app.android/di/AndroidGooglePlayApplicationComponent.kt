package com.mooncloak.vpn.app.android.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.android.info.AndroidAppClientInfo
import com.mooncloak.vpn.app.android.api.wireguard.AndroidWireGuardConnectionKeyManager
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardBackend
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardTunnelManager
import com.mooncloak.vpn.app.android.util.WireGuardVpnContextWrapper
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.app.shared.api.network.AndroidLocalNetworkManager
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.vpn.TunnelManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.storage.database.AndroidDatabaseDriverFactory
import com.mooncloak.vpn.app.shared.storage.database.DatabaseDriverFactory
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.shared.util.coroutine.ApplicationCoroutineScope
import com.wireguard.android.backend.GoBackend

@Component
@Singleton
internal abstract class AndroidGooglePlayApplicationComponent internal constructor(
    @get:Provides override val applicationContext: ApplicationContext,
    @get:Provides override val applicationCoroutineScope: ApplicationCoroutineScope
) : AndroidApplicationComponent() {

    @Provides
    @Singleton
    internal fun provideAppClientInfo(appClientInfo: AndroidAppClientInfo): AppClientInfo =
        appClientInfo

    @Provides
    @Singleton
    internal fun provideDatabaseDriverFactory(factory: AndroidDatabaseDriverFactory): DatabaseDriverFactory = factory

    @Provides
    @Singleton
    internal fun provideLocalNetworkManager(manager: AndroidLocalNetworkManager): LocalNetworkManager = manager

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
    internal fun provideTunnelManager(manager: WireGuardTunnelManager): TunnelManager = manager
}

internal fun ApplicationComponent.Companion.create(
    applicationContext: ApplicationContext,
    coroutineScope: ApplicationCoroutineScope
): AndroidGooglePlayApplicationComponent = AndroidGooglePlayApplicationComponent::class.create(
    applicationContext = applicationContext,
    applicationCoroutineScope = coroutineScope
)
