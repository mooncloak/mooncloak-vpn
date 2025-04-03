package com.mooncloak.vpn.app.android.di

import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardBackend
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.network.core.ip.invoke
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.invoke
import com.mooncloak.vpn.crypto.lunaris.provider.CryptoWalletAddressProvider
import com.mooncloak.vpn.crypto.lunaris.repository.CryptoWalletRepository
import com.mooncloak.vpn.util.notification.NotificationManager
import com.mooncloak.vpn.util.notification.invoke
import com.mooncloak.vpn.data.shared.cache.Cache
import com.mooncloak.vpn.data.shared.cache.create
import com.mooncloak.vpn.network.core.ip.PublicDeviceIPAddressProvider
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.time.Duration.Companion.seconds

public abstract class AndroidApplicationComponent public constructor() : ApplicationComponent() {

    public abstract val wireGuardBackend: WireGuardBackend

    @Provides
    @Singleton
    internal fun provideNotificationManager(context: ApplicationContext): NotificationManager =
        NotificationManager(context = context)

    @Provides
    @Singleton
    internal fun provideDeviceIpAddressProvider(
        context: ApplicationContext,
        mooncloakApi: MooncloakVpnServiceHttpApi,
        json: Json,
        coroutineScope: ApplicationCoroutineScope
    ): PublicDeviceIPAddressProvider =
        PublicDeviceIPAddressProvider(
            context = context,
            mooncloakApi = mooncloakApi,
            cache = Cache.create(
                format = json,
                maxSize = 1,
                expirationAfterWrite = 5.seconds
            ),
            coroutineScope = coroutineScope
        )

    @Provides
    @Singleton
    internal fun provideCryptoWalletApi(
        addressProvider: CryptoWalletAddressProvider,
        repository: CryptoWalletRepository,
        clock: Clock
    ): CryptoWalletManager = CryptoWalletManager(
        cryptoWalletAddressProvider = addressProvider,
        walletDirectoryPath = File(applicationContext.filesDir, CryptoWalletManager.DEFAULT_WALLET_DIRECTORY_NAME).apply {
            mkdirs()
        }.absolutePath,
        cryptoWalletRepository = repository,
        clock = clock
    )
}
