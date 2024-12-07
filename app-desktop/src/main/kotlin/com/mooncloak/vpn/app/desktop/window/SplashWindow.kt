package com.mooncloak.vpn.app.desktop.window

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState

@Composable
internal fun SplashWindow(
    onCloseRequest: () -> Unit,
    visible: Boolean
) {
    ThemedWindow(
        state = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition(Alignment.Center),
            size = DpSize(width = 600.dp, height = 400.dp)
        ),
        visible = visible,
        onCloseRequest = onCloseRequest,
        undecorated = true,
        transparent = true,
        resizable = false,
        alwaysOnTop = true
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                /* FIXME
                Image(
                    modifier = Modifier.size(128.dp),
                    painter = Resources.painters.logo,
                    contentDescription = null
                )*/

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = "Mooncloak VPN", // TODO:  Resources.strings.appName,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
