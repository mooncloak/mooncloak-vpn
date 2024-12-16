package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.UriHandler
import coil3.SingletonImageLoader
import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.storage.PreferencesStorage
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
    public val preferencesStorage: PreferencesStorage
    public val uriHandler: UriHandler
    public val imageLoaderFactory: SingletonImageLoader.Factory
    public val billingManager: BillingManager

    public companion object
}

@Composable
public fun <T> rememberApplicationDependency(getter: ApplicationDependencies.() -> T): T {
    val dependencies = LocalApplicationComponent.current

    return remember(dependencies) { getter.invoke(dependencies) }
}
