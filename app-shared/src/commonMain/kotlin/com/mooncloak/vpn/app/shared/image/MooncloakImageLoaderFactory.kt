package com.mooncloak.vpn.app.shared.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.serviceLoaderEnabled
import coil3.util.Logger
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.debug
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.kodetools.logpile.core.verbose
import com.mooncloak.kodetools.logpile.core.warning
import io.ktor.client.HttpClient

public class MooncloakImageLoaderFactory @Inject public constructor(
    private val httpClient: HttpClient
) : SingletonImageLoader.Factory {

    override fun newImageLoader(context: PlatformContext): ImageLoader =
        ImageLoader.Builder(context)
            .serviceLoaderEnabled(false)
            .components {
                add(
                    KtorNetworkFetcherFactory(
                        httpClient = httpClient
                    )
                )
            }
            .logger(object : Logger {

                override var minLevel: Logger.Level = Logger.Level.Info

                override fun log(
                    tag: String,
                    level: Logger.Level,
                    message: String?,
                    throwable: Throwable?
                ) {
                    when (level) {
                        Logger.Level.Verbose -> LogPile.verbose(
                            message = message ?: "",
                            cause = throwable,
                            tag = tag
                        )

                        Logger.Level.Debug -> LogPile.debug(
                            message = message ?: "",
                            cause = throwable,
                            tag = tag
                        )

                        Logger.Level.Info -> LogPile.info(
                            message = message ?: "",
                            cause = throwable,
                            tag = tag
                        )

                        Logger.Level.Warn -> LogPile.warning(
                            message = message ?: "",
                            cause = throwable,
                            tag = tag
                        )

                        Logger.Level.Error -> LogPile.error(
                            message = message ?: "",
                            cause = throwable,
                            tag = tag
                        )
                    }
                }
            })
            .build()
}
