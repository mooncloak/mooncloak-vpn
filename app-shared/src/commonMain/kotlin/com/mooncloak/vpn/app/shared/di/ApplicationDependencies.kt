package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.UriHandler
import coil3.SingletonImageLoader
import com.mooncloak.vpn.app.shared.app.AppClientInfo
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
public expect interface ApplicationDependencies {

    public val json: Json
    public val httpClient: HttpClient
    public val clock: Clock

    public val appClientInfo: AppClientInfo

    public val uriHandler: UriHandler

    public val imageLoaderFactory: SingletonImageLoader.Factory

    public companion object
}

@Composable
public fun <T> rememberApplicationDependency(getter: ApplicationDependencies.() -> T): T {
    val dependencies = LocalApplicationComponent.current

    return remember(dependencies) { getter.invoke(dependencies) }
}
