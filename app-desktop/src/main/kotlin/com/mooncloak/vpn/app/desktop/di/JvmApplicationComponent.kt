package com.mooncloak.vpn.app.desktop.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.desktop.api.wireguard.JvmWireGuardConnectionKeyManager
import com.mooncloak.vpn.app.desktop.api.wireguard.JvmWireGuardTunnelManager
import com.mooncloak.vpn.app.desktop.info.JvmAppClientInfo
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.api.shared.network.DeviceIPAddressProvider
import com.mooncloak.vpn.api.shared.network.LocalNetworkManager
import com.mooncloak.vpn.api.shared.network.invoke
import com.mooncloak.vpn.api.shared.vpn.TunnelManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.data.sqlite.invoke
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.notification.invoke
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.data.sqlite.SqlDriverFactory
import com.mooncloak.vpn.data.sqlite.util.getDatabaseFileLocation
import kotlinx.datetime.Clock

@Component
@Singleton
internal abstract class JvmApplicationComponent internal constructor(
    @get:Provides override val applicationCoroutineScope: ApplicationCoroutineScope
) : ApplicationComponent() {

    @Provides
    @Singleton
    internal fun provideNotificationManager(): NotificationManager =
        NotificationManager()

    @Provides
    @Singleton
    internal fun provideAppClientInfo(appClientInfo: JvmAppClientInfo): AppClientInfo =
        appClientInfo

    @Provides
    @Singleton
    internal fun provideDatabaseDriverFactory(appClientInfo: AppClientInfo): SqlDriverFactory =
        SqlDriverFactory(
            filePath = getDatabaseFileLocation(appName = appClientInfo.id),
            schema = MooncloakDatabase.Schema
        )

    @Provides
    @Singleton
    internal fun provideLocalNetworkManager(): LocalNetworkManager = LocalNetworkManager()

    @Provides
    @Singleton
    internal fun provideWireGuardConnectionKeyManager(manager: JvmWireGuardConnectionKeyManager): WireGuardConnectionKeyManager =
        manager

    @Provides
    @Singleton
    internal fun provideTunnelManager(manager: JvmWireGuardTunnelManager): TunnelManager = manager

    @Provides
    @Singleton
    internal fun provideDeviceIpAddressProvider(
        mooncloakApi: MooncloakVpnServiceHttpApi,
        clock: Clock
    ): DeviceIPAddressProvider = DeviceIPAddressProvider.invoke(
        mooncloakApi = mooncloakApi,
        clock = clock
    )
}

internal fun ApplicationComponent.Companion.create(
    applicationCoroutineScope: ApplicationCoroutineScope
): JvmApplicationComponent = JvmApplicationComponent::class.create(
    applicationCoroutineScope = applicationCoroutineScope
)
