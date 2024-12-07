package com.mooncloak.vpn.app.shared.di

import androidx.compose.ui.platform.UriHandler
import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.kodetools.storagex.keyvalue.InMemory
import com.mooncloak.kodetools.storagex.keyvalue.KeyValueStorage
import com.mooncloak.kodetools.storagex.keyvalue.MutableKeyValueStorage
import kotlinx.serialization.json.Json

@Component
@Singleton
internal abstract class WasmApplicationComponent internal constructor(
    @get:Provides override val uriHandler: UriHandler,
) : ApplicationComponent() {

    override fun provideKeyValueStorage(format: Json): MutableKeyValueStorage<String> =
        KeyValueStorage.InMemory()
}

@PublishedApi
internal fun ApplicationComponent.Companion.create(
    uriHandler: UriHandler
): WasmApplicationComponent = WasmApplicationComponent::class.create(
    uriHandler = uriHandler,
)
