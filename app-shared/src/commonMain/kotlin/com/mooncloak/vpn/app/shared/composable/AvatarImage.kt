package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_avatar
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AvatarImage(
    imageUri: String?,
    modifier: Modifier = Modifier,
    name: String? = null,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    borderWidth: Dp = 5.dp,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    calculateInitials: (name: String) -> String = {
        it.firstOrNull { char -> !char.isWhitespace() }?.lowercase() ?: ""
    },
    badge: (@Composable () -> Unit)? = null
) {
    val showName = remember { mutableStateOf(imageUri == null) }

    BoxWithConstraints(
        modifier = modifier
            .border(
                width = borderWidth,
                color = contentColor,
                shape = shape
            )
            .clip(shape)
            .background(containerColor),
        contentAlignment = Alignment.Center
    ) {
        if (!showName.value) {
            AsyncImage(
                modifier = Modifier.matchParentSize(),
                model = imageUri,
                contentDescription = stringResource(Res.string.cd_avatar),
                onError = {
                    showName.value = true
                }
            )
        } else {
            val initials = remember(name, calculateInitials) { calculateInitials.invoke(name ?: "") }

            if (initials.isNotBlank()) {
                Text(
                    text = initials,
                    style = textStyle.copy(color = contentColor),
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
            } else {
                Icon(
                    modifier = Modifier.size(minOf(maxWidth, maxHeight) / 2),
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = contentColor
                )
            }
        }
    }
}
