package com.mooncloak.vpn.app.shared.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
public actual fun SystemUi.EnableEdgeToEdge(
    statusBarStyle: SystemBarStyle,
    navigationBarStyle: SystemBarStyle,
    content: @Composable () -> Unit
) {
    // No operation on JVM
    CompositionLocalProvider(
        LocalStatusBarStyle provides statusBarStyle,
        LocalNavigationBarStyle provides navigationBarStyle
    ) {
        content.invoke()
    }
}
