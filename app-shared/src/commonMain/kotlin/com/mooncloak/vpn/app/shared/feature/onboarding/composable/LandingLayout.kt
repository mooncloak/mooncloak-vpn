package com.mooncloak.vpn.app.shared.feature.onboarding.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.app_description
import com.mooncloak.vpn.app.shared.resource.app_name
import com.mooncloak.vpn.app.shared.resource.app_slogan_line_1
import com.mooncloak.vpn.app.shared.resource.app_slogan_line_2
import com.mooncloak.vpn.app.shared.resource.ic_logo_mooncloak
import com.mooncloak.vpn.app.shared.resource.landing_action_start
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LandingLayout(
    version: String?,
    onStart: () -> Unit,
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
                ).blur(100.dp)
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
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )

            if (version != null) {
                Box(
                    modifier = Modifier.padding(top = 8.dp)
                        .clip(RoundedCornerShape(percent = 50))
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier,
                        text = version,
                        style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onTertiaryContainer),
                        maxLines = 2,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
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

                Button(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = onStart
                ) {
                    Text(text = stringResource(Res.string.landing_action_start))
                }
            }
        }
    }
}
