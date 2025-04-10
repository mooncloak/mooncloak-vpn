package com.mooncloak.vpn.app.shared

import androidx.compose.ui.window.ComposeUIViewController
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.configure
import com.mooncloak.vpn.app.shared.crypto.IosCryptoWalletManager
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.di.create
import com.mooncloak.vpn.app.shared.feature.app.ApplicationRootScreen
import com.mooncloak.vpn.app.shared.util.log.NoOpLogger
import com.mooncloak.vpn.app.shared.util.platformDefaultUriHandler
import kotlinx.coroutines.MainScope
import platform.UIKit.UIViewController

@Suppress("FunctionName")
public fun MainViewController(
    cryptoWalletManagerFactory: IosCryptoWalletManager.Factory
): UIViewController = ComposeUIViewController {
    val platformUriHandler = platformDefaultUriHandler()
    val coroutineScope = MainScope()

    val applicationDependencies = ApplicationComponent.create(
        applicationCoroutineScope = coroutineScope,
        cryptoWalletManagerFactory = cryptoWalletManagerFactory
    )
    val presentationDependencies = PresentationComponent.create(
        applicationComponent = applicationDependencies,
        presentationCoroutineScope = coroutineScope,
        uriHandler = platformUriHandler
    )

    // Disable logging if we are not in debug mode.
    if (!applicationDependencies.appClientInfo.isDebug) {
        LogPile.configure(NoOpLogger)
    }

    ApplicationRootScreen(
        applicationComponent = applicationDependencies,
        presentationComponent = presentationDependencies
    )
}
