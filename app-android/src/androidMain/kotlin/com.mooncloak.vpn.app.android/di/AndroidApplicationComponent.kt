package com.mooncloak.vpn.app.android.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.Settings
import com.mooncloak.vpn.app.android.AndroidAppClientInfo
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.util.ActivityContext
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

@Component
@Singleton
internal abstract class AndroidApplicationComponent internal constructor(
    @get:Provides override val uriHandler: UriHandler,
    @get:Provides override val activityContext: ActivityContext
) : ApplicationComponent() {

    override fun provideKeyValueStorage(format: Json): MutableKeyValueStorage<String> =
        KeyValueStorage.Settings(
            format = format,
            settings = Settings()
        )

    @Provides
    @Singleton
    internal fun provideApplicationContext(): ApplicationContext =
        activityContext.applicationContext

    @Provides
    @Singleton
    internal fun provideAppClientInfo(appClientInfo: AndroidAppClientInfo): AppClientInfo =
        appClientInfo
}

internal fun ApplicationComponent.Companion.create(
    activityContext: ActivityContext,
    uriHandler: UriHandler
): AndroidApplicationComponent = AndroidApplicationComponent::class.create(
    activityContext = activityContext,
    uriHandler = uriHandler
)
