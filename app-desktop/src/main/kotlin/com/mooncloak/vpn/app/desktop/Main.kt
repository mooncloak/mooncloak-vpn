package com.mooncloak.vpn.app.desktop

import androidx.compose.ui.window.application
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.configure
import com.mooncloak.vpn.app.desktop.di.create
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.log.NoOpLogger
import com.mooncloak.vpn.app.shared.util.platformDefaultUriHandler

public fun main(): Unit = application {
    val platformUriHandler = platformDefaultUriHandler()

    val applicationDependencies = ApplicationComponent.create()
    val presentationDependencies = PresentationComponent.create(
        applicationComponent = applicationDependencies,
        uriHandler = platformUriHandler
    )

    // Disable logging if we are not in debug mode.
    if (!applicationDependencies.appClientInfo.isDebug) {
        LogPile.configure(NoOpLogger)
    }

    ApplicationRootScreen(
        applicationComponent = applicationDependencies,
        presentationComponent = presentationDependencies,
        uriHandler = platformUriHandler
    )
}
