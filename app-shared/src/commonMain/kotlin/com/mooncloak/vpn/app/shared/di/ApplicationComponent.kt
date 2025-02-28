package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import coil3.SingletonImageLoader
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.api.shared.billing.MutableServicePurchaseReceiptRepository
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceiptDatabaseSource
import com.mooncloak.vpn.api.shared.billing.ServicePurchaseReceiptRepository
import com.mooncloak.vpn.app.shared.api.server.RegisteredClientDatabaseSource
import com.mooncloak.vpn.api.shared.server.RegisteredClientRepository
import com.mooncloak.vpn.app.shared.api.server.ServerConnectionRecordDatabaseSource
import com.mooncloak.vpn.api.shared.server.ServerConnectionRecordRepository
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.provider.HostUrlProvider
import com.mooncloak.vpn.api.shared.provider.Mooncloak
import com.mooncloak.vpn.app.shared.api.service.ServiceTokensSource
import com.mooncloak.vpn.app.shared.util.image.MooncloakImageLoaderFactory
import com.mooncloak.vpn.app.shared.database.MooncloakDatabaseProvider
import com.mooncloak.vpn.app.shared.util.http.DefaultUnauthorizedInterceptor
import com.mooncloak.vpn.app.shared.util.http.UnauthorizedInterceptor
import com.mooncloak.vpn.app.shared.util.http.interceptUnauthorized
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.data.shared.keyvalue.MutableKeyValueStorage
import com.mooncloak.vpn.data.shared.keyvalue.SettingsKeyValueStorage
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@Singleton
public abstract class ApplicationComponent : ApplicationDependencies {

    @Singleton
    @Provides
    public fun provideSettings(): Settings =
        Settings()

    @Singleton
    @Provides
    public fun provideKeyValueStorage(
        settings: Settings,
        serializersModule: SerializersModule
    ): MutableKeyValueStorage = SettingsKeyValueStorage(
        settings = settings,
        serializersModule = serializersModule
    )

    @Provides
    @Singleton
    public fun provideLogger(): Logger = LogPile

    @Provides
    @Singleton
    public fun provideJson(serializersModule: SerializersModule): Json = Json {
        ignoreUnknownKeys = true
        this.serializersModule = serializersModule
    }

    @Provides
    @Singleton
    public fun provideSerializersModule(): SerializersModule = EmptySerializersModule()

    @Provides
    public fun unauthorizedInterceptor(interceptor: DefaultUnauthorizedInterceptor): UnauthorizedInterceptor =
        interceptor

    @Provides
    public fun provideHostUrlProvider(): HostUrlProvider = HostUrlProvider.Mooncloak

    @Provides
    @Singleton
    public fun provideHttpClient(
        json: Json,
        interceptor: UnauthorizedInterceptor
    ): HttpClient = HttpClient {
        // Installs support for content negotiation with the server. This allows us to submit and
        // receive the HTTP requests and responses in a particular format (ex: JSON).
        // https://ktor.io/docs/client-serialization.html
        install(ContentNegotiation) {
            json(json)
        }

        // Installs an HTTP cache. Currently, only an in-memory cache is supported. This makes the
        // same HTTP requests respond with the cached data instead of requiring to make the call
        // again, resulting in a performance boost.
        // https://ktor.io/docs/client-caching.html
        // FIXME: Re-enable HTTP Cache: install(HttpCache)
        // The HTTP Cache is causing issues, specifically the following error on load from the cache:
        // `java.lang.IllegalStateException: No instance for key AttributeKey: CallLogger`
        // It incorrectly points to the `CallLogger` component, even though it is in fact the HttpCache.
        // Re-enable when you figure out how to fix this. Note, the call that always seems to fail is the call to load
        // all the contributors.

        // Installs support for content encoding. This handles compression and decompression of
        // HTTP requests and responses automatically for the supported algorithms.
        // https://ktor.io/docs/client-content-encoding.html
        install(ContentEncoding) {
            identity()
            deflate()
            gzip()
        }

        // Installs logging for HTTP requests and responses. These are only logged during
        // development because our logger (LogPile) only logs when in "debug" mode. Seeing these
        // logs helps us determine issues within the application (like why an API request failed).
        // https://ktor.io/docs/client-logging.html#configure_plugin
        install(Logging) {
            logger = object : io.ktor.client.plugins.logging.Logger {

                override fun log(message: String) {
                    LogPile.info(message)
                }
            }

            level = LogLevel.ALL

            // Remove the Authorization header value from being logged even in development.
            // https://ktor.io/docs/client-logging.html#configure_plugin
            sanitizeHeader { header ->
                header == HttpHeaders.Authorization
            }
        }
    }.apply {
        interceptUnauthorized(
            interceptor = interceptor
        )
    }

    @Provides
    public fun provideMooncloakVpnServiceHttpApi(
        httpClient: HttpClient,
        hostUrlProvider: HostUrlProvider
    ): MooncloakVpnServiceHttpApi =
        MooncloakVpnServiceHttpApi(
            httpClient = httpClient,
            hostUrlProvider = hostUrlProvider
        )

    @Provides
    @Singleton
    public fun provideImageLoaderFactory(
        factory: MooncloakImageLoaderFactory
    ): SingletonImageLoader.Factory = factory

    @Provides
    @Singleton
    public fun provideClock(): Clock = Clock.System

    @Provides
    @Singleton
    public fun provideServiceTokensRepository(source: ServiceTokensSource): ServiceTokensRepository =
        source

    @Provides
    @Singleton
    public fun provideServicePurchaseReceiptRepository(source: ServicePurchaseReceiptDatabaseSource): ServicePurchaseReceiptRepository =
        source

    @Provides
    @Singleton
    public fun provideMutableServicePurchaseReceiptRepository(source: ServicePurchaseReceiptDatabaseSource): MutableServicePurchaseReceiptRepository =
        source

    @Provides
    @Singleton
    public fun provideServerConnectionRecordRepository(source: ServerConnectionRecordDatabaseSource): ServerConnectionRecordRepository =
        source

    @Provides
    @Singleton
    public fun provideRegisteredClientRepository(source: RegisteredClientDatabaseSource): RegisteredClientRepository =
        source

    @Provides
    @Singleton
    public fun provideDatabase(provider: MooncloakDatabaseProvider): MooncloakDatabase = provider.get()

    public companion object
}

/**
 * Provides access to the [ApplicationComponent] from [Composable] functions.
 */
internal val LocalApplicationComponent: ProvidableCompositionLocal<ApplicationComponent> =
    staticCompositionLocalOf { error("No ApplicationComponent was provided.") }
