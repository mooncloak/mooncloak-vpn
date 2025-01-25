package com.mooncloak.vpn.web.download

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import com.mooncloak.vpn.web.download.info.WebBuildConfig
import com.mooncloak.vpn.web.download.resource.Res
import com.mooncloak.vpn.web.download.resource.app_name
import com.mooncloak.vpn.web.download.resource.app_not_released
import com.mooncloak.vpn.web.download.resource.app_slogan_line_1
import com.mooncloak.vpn.web.download.resource.app_slogan_line_2
import com.mooncloak.vpn.web.download.resource.google_play_preregister
import com.mooncloak.vpn.web.download.resource.ic_logo_mooncloak
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
public fun DownloadScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    MooncloakTheme {
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
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    modifier = Modifier.padding(top = 32.dp)
                        .size(96.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(ColorPalette.MooncloakDarkPrimary),
                    painter = painterResource(Res.drawable.ic_logo_mooncloak),
                    contentDescription = null
                )

                FlowRow(
                    modifier = Modifier.sizeIn(maxWidth = 600.dp)
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize()
                            .padding(horizontal = 32.dp)
                            .align(Alignment.CenterVertically),
                        text = stringResource(Res.string.app_name),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            lineBreak = LineBreak.Heading
                        ),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )

                    if (WebBuildConfig.playStoreUrl != null || WebBuildConfig.directDownloadUrl != null) {
                        Box(
                            modifier = Modifier.wrapContentSize()
                                .align(Alignment.CenterVertically)
                                .clip(RoundedCornerShape(percent = 50))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier,
                                text = WebBuildConfig.appVersion,
                                style = MaterialTheme.typography.labelLarge.copy(color = MaterialTheme.colorScheme.onTertiaryContainer),
                                maxLines = 2,
                                overflow = TextOverflow.Clip,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else if (WebBuildConfig.playStorePreRegisterUrl != null) {
                        Image(
                            modifier = Modifier.sizeIn(minWidth = 200.dp, maxWidth = 400.dp)
                                .pointerHoverIcon(PointerIcon.Hand)
                                .clickable { },
                            painter = painterResource(Res.drawable.google_play_preregister),
                            contentDescription = "Pre-register on Google Play",
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

                if (WebBuildConfig.playStoreUrl != null) {
                    AsyncImage(
                        modifier = Modifier.width(200.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 24.dp)
                            .padding(horizontal = 16.dp)
                            .clickable {
                                uriHandler.openUri(WebBuildConfig.playStoreUrl)
                            }
                            .pointerHoverIcon(PointerIcon.Hand),
                        contentScale = ContentScale.FillWidth,
                        model = "https://cdn.mooncloak.com/app/latest/assets/image/google-play/GetItOnGooglePlay_Badge_Web_color_English.png",
                        contentDescription = "Get on Google Play"
                    )
                }

                if (WebBuildConfig.playStoreUrl == null && WebBuildConfig.directDownloadUrl == null) {
                    Text(
                        modifier = Modifier.padding(top = 32.dp),
                        text = stringResource(Res.string.app_not_released),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color.Black.copy(
                                alpha = 0.68f
                            )
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 32.dp),
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

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
