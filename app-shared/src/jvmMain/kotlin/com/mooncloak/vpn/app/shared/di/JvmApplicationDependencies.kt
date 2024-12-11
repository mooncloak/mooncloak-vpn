package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import coil3.SingletonImageLoader
import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.vpn.app.shared.feature.app.AppClientInfo
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public actual interface ApplicationDependencies {

    public actual val json: Json
    public actual val httpClient: HttpClient
    public actual val clock: Clock
    public actual val logger: Logger
    public actual val appClientInfo: AppClientInfo
    public actual val uriHandler: UriHandler
    public actual val imageLoaderFactory: SingletonImageLoader.Factory

    public actual companion object
}
