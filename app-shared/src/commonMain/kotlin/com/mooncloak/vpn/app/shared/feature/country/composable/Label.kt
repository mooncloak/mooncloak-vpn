package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun Label(
    value: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
) {
    Text(
        modifier = modifier,
        text = value,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = style,
        color = color,
        textAlign = TextAlign.Start
    )
}
