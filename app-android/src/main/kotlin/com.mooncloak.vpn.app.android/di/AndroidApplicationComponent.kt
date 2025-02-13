package com.mooncloak.vpn.app.android.di

import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.android.api.wireguard.WireGuardBackend
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.util.ApplicationContext
import com.mooncloak.vpn.app.shared.util.notification.NotificationManager
import com.mooncloak.vpn.app.shared.util.notification.invoke

public abstract class AndroidApplicationComponent public constructor() : ApplicationComponent() {

    public abstract val wireGuardBackend: WireGuardBackend

    @Provides
    @Singleton
    internal fun provideNotificationManager(context: ApplicationContext): NotificationManager =
        NotificationManager(context = context)
}
