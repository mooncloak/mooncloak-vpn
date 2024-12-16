package com.mooncloak.vpn.app.shared.di

import android.app.Activity
import androidx.compose.ui.platform.UriHandler
import coil3.SingletonImageLoader
import com.mooncloak.kodetools.logpile.core.Logger
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.storage.PreferencesStorage
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.app.shared.util.ApplicationContext
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
    public actual val preferencesStorage: PreferencesStorage
    public actual val uriHandler: UriHandler
    public actual val imageLoaderFactory: SingletonImageLoader.Factory
    public actual val billingManager: BillingManager

    public val applicationContext: ApplicationContext
    public val activityContext: ActivityContext
    public val activity: Activity

    public actual companion object
}
