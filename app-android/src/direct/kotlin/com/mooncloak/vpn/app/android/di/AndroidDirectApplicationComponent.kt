package com.mooncloak.vpn.app.android.di

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
internal abstract class AndroidDirectApplicationComponent internal constructor(
    @get:Provides override val applicationContext: ApplicationContext
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

    @Provides
    @Singleton
    internal fun provideDatabaseDriverFactory(factory: AndroidDatabaseDriverFactory): DatabaseDriverFactory = factory
}

internal fun ApplicationComponent.Companion.create(
    applicationContext: ApplicationContext
): AndroidDirectApplicationComponent = AndroidDirectApplicationComponent::class.create(
    applicationContext = applicationContext
)
