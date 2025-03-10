package com.mooncloak.vpn.app.android.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.Settings
import com.mooncloak.vpn.app.android.AndroidAppClientInfo
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json
import com.mooncloak.vpn.app.shared.api.network.AndroidLocalNetworkManager
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.api.vpn.TunnelManager
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardTunnelManager
import com.mooncloak.vpn.app.shared.util.coroutine.ApplicationCoroutineScope

@Component
@Singleton
internal abstract class AndroidDirectApplicationComponent internal constructor(
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
    internal fun provideWireGuardBackend(context: ApplicationContext): WireGuardBackend = GoBackend(context)

    @Provides
    @Singleton
    internal fun provideTunnelManager(manager: WireGuardTunnelManager): TunnelManager = manager
}

internal fun ApplicationComponent.Companion.create(
    applicationContext: ApplicationContext,
    coroutineScope: ApplicationCoroutineScope
): AndroidDirectApplicationComponent = AndroidDirectApplicationComponent::class.create(
    applicationContext = applicationContext,
    applicationCoroutineScope = coroutineScope
)
