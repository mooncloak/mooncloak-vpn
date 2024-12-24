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
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import com.mooncloak.vpn.app.shared.api.service.ServiceAccessDetailsRepository
import com.mooncloak.vpn.app.shared.api.service.ServiceAccessDetailsDatabaseSource
import com.mooncloak.vpn.app.shared.image.MooncloakImageLoaderFactory
import com.mooncloak.vpn.app.shared.storage.database.DatabaseDriverFactory
import com.mooncloak.vpn.app.shared.storage.database.MooncloakDatabaseProvider
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
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
    public abstract fun provideKeyValueStorage(format: Json): MutableKeyValueStorage<String>

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
    @Singleton
    public fun provideHttpClient(
        json: Json,
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
        install(HttpCache)

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
    }

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
    public fun provideServiceAccessDetailsRepository(source: ServiceAccessDetailsDatabaseSource): ServiceAccessDetailsRepository =
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
