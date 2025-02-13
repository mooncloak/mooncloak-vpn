package com.mooncloak.vpn.app.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.configure
import com.mooncloak.vpn.app.desktop.di.create
import com.mooncloak.vpn.app.desktop.window.MainWindow
import com.mooncloak.vpn.app.desktop.window.SplashWindow
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.util.log.NoOpLogger
import com.mooncloak.vpn.app.shared.util.platformDefaultUriHandler
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

public fun main(): Unit = application {
    val platformUriHandler = platformDefaultUriHandler()
    val coroutineScope = MainScope()

    val applicationDependencies = ApplicationComponent.create(
        applicationCoroutineScope = coroutineScope
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

    val minWindowSize = remember { DpSize(width = 300.dp, height = 300.dp) }
    val windowState = rememberWindowState(
        size = DpSize(width = 800.dp, height = 700.dp)
    )
    LaunchedEffect(windowState.size) {
        val currentSize = windowState.size
        val width = currentSize.width.coerceAtLeast(minWindowSize.width)
        val height = currentSize.height.coerceAtLeast(minWindowSize.height)

        // TODO: Figure a way to make this look less choppy. But at least we protect against the size getting too small.
        if (width != currentSize.width || height != currentSize.height) {
            windowState.size = DpSize(
                width = width,
                height = height
            )
        }
    }

    val displaySplashScreen = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2.seconds)

        displaySplashScreen.value = false
    }

    SplashWindow(
        name = applicationDependencies.appClientInfo.name,
        visible = displaySplashScreen.value,
        onCloseRequest = {
            displaySplashScreen.value = false
        }
    )

    MainWindow(
        applicationDependencies = applicationDependencies,
        presentationDependencies = presentationDependencies,
        state = windowState,
        visible = !displaySplashScreen.value,
        onClose = ::exitApplication
    )
}
