package com.mooncloak.vpn.app.shared.feature.collaborator.list.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun NoCollaboratorsCard(
    title: String,
    description: String? = null,
    icon: Painter,
    error: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderGraphic(
                modifier = Modifier.size(64.dp)
                    .align(Alignment.CenterHorizontally),
                painter = icon,
                containerColor = if (error) {
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = SecondaryAlpha)
                } else {
                    MaterialTheme.colorScheme.background
                },
                contentColor = if (error) {
                    MaterialTheme.colorScheme.onErrorContainer
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            if (description != null) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = SecondaryAlpha
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun HeaderGraphic(
    painter: Painter,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    containerColor: Color = MaterialTheme.colorScheme.errorContainer.copy(alpha = SecondaryAlpha),
    contentColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    contentDescription: String? = null
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.matchParentSize()
                .clip(shape)
                .background(containerColor)
        )

        val iconSize = minOf(maxWidth, maxHeight) / 2

        Icon(
            modifier = Modifier.size(iconSize),
            painter = painter,
            contentDescription = contentDescription,
            tint = contentColor
        )
    }
}
