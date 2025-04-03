package com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun LabeledContent(
    label: String,
    value: String,
    valueColor: Color,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp)
                        .size(24.dp),
                    imageVector = icon,
                    contentDescription = null,
                    tint = valueColor
                )
            }

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = valueColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
            maxLines = 2,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center
        )
    }
}
