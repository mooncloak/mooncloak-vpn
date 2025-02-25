package com.mooncloak.vpn.app.android.di

import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardBackend
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.network.DeviceIPAddressProvider
import com.mooncloak.vpn.api.shared.network.invoke
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.notification.invoke
import kotlinx.datetime.Clock

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
        clock: Clock
    ): DeviceIPAddressProvider =
        DeviceIPAddressProvider(
            context = context,
            mooncloakApi = mooncloakApi,
            clock = clock
        )
}
