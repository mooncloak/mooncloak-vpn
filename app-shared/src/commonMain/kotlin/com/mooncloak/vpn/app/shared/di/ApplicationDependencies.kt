package com.mooncloak.vpn.app.shared.di

import coil3.SingletonImageLoader
import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.app.shared.api.server.usecase.GetDefaultServerUseCase
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import com.mooncloak.vpn.crypto.lunaris.CryptoPasswordManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.notification.NotificationManager
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueStorage
import com.mooncloak.vpn.data.sqlite.SqlDriverFactory
import com.mooncloak.vpn.network.core.ip.LocalDeviceIPAddressProvider
import com.mooncloak.vpn.network.core.ip.PublicDeviceIPAddressProvider
import com.mooncloak.vpn.util.shortcuts.AppShortcutManager
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect interface ApplicationDependencies {

    public val serializersModule: SerializersModule
    public val json: Json
    public val httpClient: HttpClient
    public val clock: Clock
    public val logger: Logger
    public val appClientInfo: AppClientInfo
    public val keyValueStorage: MutableKeyValueStorage
    public val preferenceStorage: UserPreferenceSettings
    public val imageLoaderFactory: SingletonImageLoader.Factory
    public val databaseDriverFactory: SqlDriverFactory
    public val localDeviceIPAddressProvider: LocalDeviceIPAddressProvider
    public val deviceIPAddressProvider: PublicDeviceIPAddressProvider
    public val wireGuardConnectionKeyManager: WireGuardConnectionKeyManager
    public val tunnelManager: TunnelManager
    public val applicationCoroutineScope: ApplicationCoroutineScope
    public val notificationManager: NotificationManager
    public val getDefaultServer: GetDefaultServerUseCase
    public val appShortcutManager: AppShortcutManager
    public val cryptoWalletManager: CryptoWalletManager
    public val cryptoPasswordManager: CryptoPasswordManager

    public companion object
}
