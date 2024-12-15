package com.mooncloak.vpn.app.desktop.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.Settings
import com.mooncloak.vpn.app.desktop.DesktopAppClientInfo
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

@Component
@Singleton
internal abstract class JvmApplicationComponent internal constructor(
    @get:Provides override val uriHandler: UriHandler,
) : ApplicationComponent() {

    override fun provideKeyValueStorage(format: Json): MutableKeyValueStorage<String> =
        KeyValueStorage.Settings(
            format = format,
            settings = Settings()
        )

    @Provides
    @Singleton
    internal fun provideAppClientInfo(appClientInfo: DesktopAppClientInfo): AppClientInfo =
        appClientInfo
}

internal fun ApplicationComponent.Companion.create(
    uriHandler: UriHandler
): JvmApplicationComponent = JvmApplicationComponent::class.create(
    uriHandler = uriHandler
)
