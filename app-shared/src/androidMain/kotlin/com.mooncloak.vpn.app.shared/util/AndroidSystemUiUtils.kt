package com.mooncloak.vpn.app.shared.util

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext

@Composable
@SuppressLint("ComposableNaming")
public actual fun SystemUi.EnableEdgeToEdge(
    statusBarStyle: SystemBarStyle,
    navigationBarStyle: SystemBarStyle,
    content: @Composable () -> Unit
) {
    val activity = LocalContext.current as? ComponentActivity

    activity?.enableEdgeToEdge(
        statusBarStyle = statusBarStyle.toAndroidSystemBarStyle(),
        navigationBarStyle = navigationBarStyle.toAndroidSystemBarStyle()
    )

    CompositionLocalProvider(
        LocalStatusBarStyle provides statusBarStyle,
        LocalNavigationBarStyle provides navigationBarStyle
    ) {
        content.invoke()
    }
}

private fun SystemBarStyle.toAndroidSystemBarStyle(): androidx.activity.SystemBarStyle =
    when (this.mode) {
        SystemBarStyle.Mode.Light -> androidx.activity.SystemBarStyle.light(
            scrim = this.lightScrim.toArgb(),
            darkScrim = this.darkScrim.toArgb()
        )

        SystemBarStyle.Mode.Dark -> androidx.activity.SystemBarStyle.dark(
            scrim = this.darkScrim.toArgb()
        )

        SystemBarStyle.Mode.System -> androidx.activity.SystemBarStyle.auto(
            lightScrim = this.lightScrim.toArgb(),
            darkScrim = this.darkScrim.toArgb()
        )
    }
