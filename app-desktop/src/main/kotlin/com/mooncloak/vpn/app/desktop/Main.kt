package com.mooncloak.vpn.app.desktop

import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.window.application
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.configure
import com.mooncloak.vpn.app.desktop.di.create
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.log.NoOpLogger

public fun main(): Unit = application {
    val uriHandler = object : UriHandler {
        override fun openUri(uri: String) {
            // TODO
        }
    }// FIXME: LocalUriHandler.current

    val applicationDependencies = ApplicationComponent.create(
        uriHandler = uriHandler
    )

    // Disable logging if we are not in debug mode.
    if (!applicationDependencies.appClientInfo.isDebug) {
        LogPile.configure(NoOpLogger)
    }

    ApplicationRootScreen(
        component = applicationDependencies
    )
}
