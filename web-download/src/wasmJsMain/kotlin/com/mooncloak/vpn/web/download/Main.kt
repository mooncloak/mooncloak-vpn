package com.mooncloak.vpn.web.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow

// RUN: ./gradlew :$moduleName:wasmJsRun (ex: ./gradlew :web-download:wasmJsRun)
@OptIn(ExperimentalComposeUiApi::class)
public fun main() {
    CanvasBasedWindow(
        title = "mooncloak VPN",
        canvasElementId = "root"
    ) {
        DownloadScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}
