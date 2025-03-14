package com.mooncloak.vpn.app.shared.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.wireguard.IosWireGuardConnectionKeyManager
import com.mooncloak.vpn.app.shared.api.wireguard.IosWireGuardTunnelManager
import com.mooncloak.vpn.network.core.ip.invoke
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.info.invoke
import com.mooncloak.vpn.data.sqlite.invoke
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.notification.NotificationManager
import com.mooncloak.vpn.util.notification.invoke
import com.mooncloak.vpn.data.shared.cache.Cache
import com.mooncloak.vpn.data.shared.cache.create
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.data.sqlite.SqlDriverFactory
import com.mooncloak.vpn.network.core.ip.LocalDeviceIPAddressProvider
import com.mooncloak.vpn.network.core.ip.PublicDeviceIPAddressProvider
import com.mooncloak.vpn.util.shortcuts.AppShortcutManager
import com.mooncloak.vpn.util.shortcuts.invoke
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

@Component
@Singleton
internal abstract class IosApplicationComponent internal constructor(
    @get:Provides override val applicationCoroutineScope: ApplicationCoroutineScope,
    @get:Provides override val wireGuardConnectionKeyManager: IosWireGuardConnectionKeyManager,
    @get:Provides override val tunnelManager: IosWireGuardTunnelManager
) : ApplicationComponent() {

    @Provides
    @Singleton
    internal fun provideNotificationManager(): NotificationManager =
        NotificationManager()

    @Provides
    @Singleton
    internal fun provideAppClientInfo(): AppClientInfo = AppClientInfo()

    @Provides
    @Singleton
    internal fun provideDatabaseDriverFactory(appClientInfo: AppClientInfo): SqlDriverFactory =
        SqlDriverFactory(
            fileName = "${appClientInfo.id}.db",
            schema = MooncloakDatabase.Schema
        )

    @Provides
    @Singleton
    internal fun provideLocalNetworkManager(): LocalDeviceIPAddressProvider =
        LocalDeviceIPAddressProvider()

    @Provides
    @Singleton
    internal fun provideDeviceIpAddressProvider(
        mooncloakApi: MooncloakVpnServiceHttpApi,
        json: Json
    ): PublicDeviceIPAddressProvider = PublicDeviceIPAddressProvider.invoke(
        mooncloakApi = mooncloakApi,
        cache = Cache.create(
            format = json,
            maxSize = 1,
            expirationAfterWrite = 5.seconds
        )
    )

    @Provides
    @Singleton
    internal fun provideAppShortcutManager(): AppShortcutManager = AppShortcutManager()
}

internal expect fun ApplicationComponent.Companion.create(
    applicationCoroutineScope: ApplicationCoroutineScope,
    wireGuardConnectionKeyManager: IosWireGuardConnectionKeyManager,
    wireGuardTunnelManager: IosWireGuardTunnelManager
): IosApplicationComponent
