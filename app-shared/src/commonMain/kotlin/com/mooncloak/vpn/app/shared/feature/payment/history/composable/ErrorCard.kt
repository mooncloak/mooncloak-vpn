package com.mooncloak.vpn.app.shared.feature.payment.history.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun ErrorCard(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            leadingContent = {
                ErrorGraphic(
                    modifier = Modifier.size(36.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = rememberVectorPainter(Icons.Default.Error)
                )
            }
        )
    }
}

@Composable
private fun ErrorGraphic(
    painter: Painter,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
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
                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = SecondaryAlpha))
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
