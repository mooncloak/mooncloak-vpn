package com.mooncloak.vpn.app.desktop.window

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import org.jetbrains.compose.resources.painterResource
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.ic_logo_mooncloak
import com.mooncloak.vpn.app.shared.theme.ColorPalette

@Composable
internal fun SplashWindow(
    name: String = "mooncloak VPN",
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
            Box(
                modifier = Modifier.fillMaxSize()
                    .shaderBackground(
                        MeshGradient(
                            colors = arrayOf(
                                ColorPalette.MooncloakDarkPrimary,
                                ColorPalette.Purple_600,
                                ColorPalette.MooncloakYellow
                            ),
                            scale = 1.5f
                        )
                    )
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(128.dp),
                    painter = painterResource(Res.drawable.ic_logo_mooncloak),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = name,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}
