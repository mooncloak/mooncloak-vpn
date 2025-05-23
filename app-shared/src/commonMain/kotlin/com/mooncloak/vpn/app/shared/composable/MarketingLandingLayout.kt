package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_name
import com.mooncloak.vpn.app.shared.resource.app_slogan_line_1
import com.mooncloak.vpn.app.shared.resource.app_slogan_line_2
import com.mooncloak.vpn.app.shared.resource.ic_logo_mooncloak
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MarketingLandingLayout(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier) {
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
            modifier = Modifier.fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.padding(top = 48.dp)
                    .size(96.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(ColorPalette.MooncloakDarkPrimary),
                painter = painterResource(Res.drawable.ic_logo_mooncloak),
                contentDescription = null
            )

            Text(
                modifier = Modifier.padding(top = 32.dp)
                    .padding(horizontal = 16.dp),
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp,
                    lineHeight = 48.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(64.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(Res.string.app_slogan_line_1),
                        maxLines = 2,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = ColorPalette.MooncloakDarkPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp,
                            lineHeight = 64.sp
                        )
                    )

                    Text(
                        text = stringResource(Res.string.app_slogan_line_2),
                        maxLines = 2,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp,
                            lineHeight = 64.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Block\nthe\nData\nCreeps",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp,
                            lineHeight = 64.sp
                        ),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}
