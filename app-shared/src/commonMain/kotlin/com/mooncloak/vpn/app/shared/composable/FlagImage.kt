package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_image_flag
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FlagImage(
    imageUri: String?,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val showIcon = remember { mutableStateOf(imageUri == null) }

    Box(
        modifier = modifier
            .aspectRatio(4f / 3f)
            .clip(RoundedCornerShape(4.dp))
            .background(containerColor)
    ) {
        if (!showIcon.value) {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = imageUri,
                contentDescription = stringResource(Res.string.cd_image_flag),
                onError = {
                    showIcon.value = true
                }
            )
        } else {
            Icon(
                modifier = Modifier.size(24.dp)
                    .align(Alignment.Center),
                imageVector = Icons.Default.Flag,
                contentDescription = stringResource(Res.string.cd_image_flag),
                tint = contentColor.copy(alpha = SecondaryAlpha)
            )
        }
    }
}
