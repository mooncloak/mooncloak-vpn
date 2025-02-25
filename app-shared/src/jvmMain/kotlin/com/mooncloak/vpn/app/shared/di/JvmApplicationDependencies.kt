package com.mooncloak.vpn.app.shared.di

import coil3.SingletonImageLoader
import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.vpn.api.shared.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.api.shared.network.DeviceIPAddressProvider
import com.mooncloak.vpn.api.shared.network.LocalNetworkManager
import com.mooncloak.vpn.api.shared.vpn.TunnelManager
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.settings.UserPreferenceSettings
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueStorage
import com.mooncloak.vpn.data.sqlite.SqlDriverFactory
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual interface ApplicationDependencies {

    public actual val serializersModule: SerializersModule
    public actual val json: Json
    public actual val httpClient: HttpClient
    public actual val clock: Clock
    public actual val logger: Logger
    public actual val appClientInfo: AppClientInfo
    public actual val keyValueStorage: MutableKeyValueStorage
    public actual val preferenceStorage: UserPreferenceSettings
    public actual val imageLoaderFactory: SingletonImageLoader.Factory
    public actual val databaseDriverFactory: SqlDriverFactory
    public actual val localNetworkManager: LocalNetworkManager
    public actual val deviceIPAddressProvider: DeviceIPAddressProvider
    public actual val wireGuardConnectionKeyManager: WireGuardConnectionKeyManager
    public actual val tunnelManager: TunnelManager
    public actual val applicationCoroutineScope: ApplicationCoroutineScope
    public actual val notificationManager: NotificationManager

    public actual companion object
}
