package com.mooncloak.vpn.app.shared.feature.collaborator.list.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
internal fun AvatarImage(
    imageUri: String?,
    modifier: Modifier = Modifier,
    name: String? = null,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    initials: (name: String) -> String = { it.firstOrNull { char -> !char.isWhitespace() }?.lowercase() ?: "" }
) {
    val showName = remember { mutableStateOf(imageUri == null) }

    BoxWithConstraints(
        modifier = modifier
            .border(
                width = 2.dp,
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
                contentDescription = null,
                onError = {
                    showName.value = true
                }
            )
        } else {
            if (name != null) {
                Text(
                    text = initials.invoke(name),
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
