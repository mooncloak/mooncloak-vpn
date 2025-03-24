package com.mooncloak.vpn.app.android.di

import android.app.Activity
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.android.api.server.AndroidVPNConnectionManager
import com.mooncloak.vpn.network.core.tunnel.TunnelManager
import com.mooncloak.vpn.network.core.tunnel.TunnelManagerPreparer
import com.mooncloak.vpn.network.core.tunnel.invoke
import com.mooncloak.vpn.network.core.vpn.VPNConnectionManager
import com.mooncloak.vpn.app.android.util.AndroidAppShortcutProvider
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.PresentationScoped
import com.mooncloak.vpn.app.shared.util.AndroidLibsLoader
import com.mooncloak.vpn.app.shared.feature.dependency.util.LibsLoader
import com.mooncloak.vpn.app.shared.util.AndroidSystemAuthenticationProvider
import com.mooncloak.vpn.app.shared.util.SystemAuthenticationProvider
import com.mooncloak.vpn.util.permission.PermissionHandler
import com.mooncloak.vpn.util.permission.invoke
import com.mooncloak.vpn.util.shortcuts.AppShortcutProvider

internal abstract class AndroidPresentationComponent internal constructor() : PresentationComponent() {

    @Provides
    @PresentationScoped
    internal fun provideSystemAuthenticationProvider(provider: AndroidSystemAuthenticationProvider): SystemAuthenticationProvider =
        provider

    @Provides
    @PresentationScoped
    internal fun provideServerConnectionManager(manager: AndroidVPNConnectionManager): VPNConnectionManager =
        manager

    @Provides
    @PresentationScoped
    internal fun provideLibsLoader(loader: AndroidLibsLoader): LibsLoader = loader

    @Provides
    @PresentationScoped
    internal fun provideTunnelManagerPreparer(tunnelManager: TunnelManager): TunnelManagerPreparer =
        TunnelManagerPreparer(
            tunnelManager = tunnelManager,
            activity = activity
        )

    @Provides
    @PresentationScoped
    internal fun provideAppShortcutProvider(provider: AndroidAppShortcutProvider): AppShortcutProvider = provider

    @Provides
    @PresentationScoped
    internal fun providePermissionHandler(): PermissionHandler = PermissionHandler(activity = activity)

    @Provides
    @PresentationScoped
    internal fun provideActivity(): Activity = activity
}
