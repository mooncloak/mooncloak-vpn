package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun HomeCardHeader(
    leadingText: String,
    modifier: Modifier = Modifier,
    trailingText: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    leadingTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    trailingTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    leadingIconSize: Dp = 24.dp,
    trailingIconSize: Dp = 24.dp,
    leadingContentColor: Color = MaterialTheme.colorScheme.onSurface,
    trailingContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                Icon(
                    modifier = Modifier.size(leadingIconSize),
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = leadingContentColor
                )
            }

            Text(
                modifier = Modifier.padding(start = if (leadingIcon != null) 8.dp else 0.dp),
                text = leadingText,
                style = leadingTextStyle.copy(color = leadingContentColor),
                maxLines = 1,
                overflow = TextOverflow.Clip
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (trailingText != null) {
                Text(
                    modifier = Modifier.padding(end = if (trailingIcon != null) 8.dp else 0.dp),
                    text = trailingText,
                    style = trailingTextStyle.copy(color = trailingContentColor),
                    maxLines = 2,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Center
                )
            }

            if (trailingIcon != null) {
                Icon(
                    modifier = Modifier.size(trailingIconSize),
                    imageVector = trailingIcon,
                    contentDescription = null,
                    tint = trailingContentColor
                )
            }
        }
    }
}
