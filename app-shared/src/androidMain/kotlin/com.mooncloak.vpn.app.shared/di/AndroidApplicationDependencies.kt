package com.mooncloak.vpn.app.shared.di

import coil3.SingletonImageLoader
import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.vpn.app.shared.api.key.WireGuardConnectionKeyManager
import com.mooncloak.vpn.app.shared.api.network.LocalNetworkManager
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.storage.KeyValueStorage
import com.mooncloak.vpn.app.shared.storage.database.DatabaseDriverFactory
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.shared.util.coroutine.ApplicationCoroutineScope
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
    public actual val keyValueStorage: KeyValueStorage
    public actual val imageLoaderFactory: SingletonImageLoader.Factory
    public actual val databaseDriverFactory: DatabaseDriverFactory
    public actual val localNetworkManager: LocalNetworkManager
    public actual val wireGuardConnectionKeyManager: WireGuardConnectionKeyManager
    public actual val applicationCoroutineScope: ApplicationCoroutineScope

    public val applicationContext: ApplicationContext

    public actual companion object
}
