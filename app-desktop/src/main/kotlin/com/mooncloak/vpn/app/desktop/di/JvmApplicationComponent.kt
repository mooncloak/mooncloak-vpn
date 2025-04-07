package com.mooncloak.vpn.app.desktop.di

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.desktop.api.wireguard.JvmWireGuardConnectionKeyManager
import com.mooncloak.vpn.app.desktop.api.wireguard.JvmWireGuardTunnelManager
import com.mooncloak.vpn.app.desktop.info.JvmAppClientInfo
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.network.core.ip.invoke
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.invoke
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.crypto.lunaris.CryptoPasswordManager
import com.mooncloak.vpn.crypto.lunaris.walletDirectory
import com.mooncloak.vpn.data.sqlite.invoke
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.notification.NotificationManager
import com.mooncloak.vpn.util.notification.invoke
import com.mooncloak.vpn.data.shared.cache.Cache
import com.mooncloak.vpn.data.shared.cache.create
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.data.sqlite.SqlDriverFactory
import com.mooncloak.vpn.data.sqlite.util.getDatabaseFileLocation
import com.mooncloak.vpn.network.core.ip.LocalDeviceIPAddressProvider
import com.mooncloak.vpn.network.core.ip.PublicDeviceIPAddressProvider
import com.mooncloak.vpn.util.shared.crypto.AesEncryptor
import com.mooncloak.vpn.util.shared.crypto.invoke
import com.mooncloak.vpn.util.shortcuts.AppShortcutManager
import com.mooncloak.vpn.util.shortcuts.invoke
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

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
    internal fun provideLocalNetworkManager(): LocalDeviceIPAddressProvider = LocalDeviceIPAddressProvider()

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

    @Provides
    @Singleton
    internal fun provideCryptoPasswordManager(): CryptoPasswordManager = CryptoPasswordManager()

    @Provides
    @Singleton
    internal fun provideCryptoWalletManager(
        addressProvider: CryptoWalletAddressProvider,
        repository: CryptoWalletRepository,
        clock: Clock
    ): CryptoWalletManager = CryptoWalletManager(
        cryptoWalletAddressProvider = addressProvider,
        walletDirectoryPath = CryptoWalletManager.walletDirectory().absolutePath,
        cryptoWalletRepository = repository,
        clock = clock,
        encryptor = AesEncryptor()
    )
}

internal fun ApplicationComponent.Companion.create(
    applicationCoroutineScope: ApplicationCoroutineScope
): JvmApplicationComponent = JvmApplicationComponent::class.create(
    applicationCoroutineScope = applicationCoroutineScope
)
