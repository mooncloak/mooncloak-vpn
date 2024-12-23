package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
public fun MooncloakIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 36.dp,
    contentPadding: PaddingValues = PaddingValues(8.dp),
    contentDescription: String? = null,
    tooltipText: String? = contentDescription,
    shape: Shape = RoundedCornerShape(5.dp),
    tint: Color = LocalContentColor.current
) {
    if (tooltipText != null) {
        TooltipBox(
            text = tooltipText
        ) {
            Icon(
                modifier = modifier.then(
                    Modifier.size(size)
                        .clip(shape)
                        .clickable(enabled) {
                            onClick.invoke()
                        }
                        .padding(contentPadding)
                ),
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint
            )
        }
    } else {
        Icon(
            modifier = modifier.then(
                Modifier.size(size)
                    .clip(shape)
                    .clickable(enabled) {
                        onClick.invoke()
                    }
                    .padding(contentPadding)
            ),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
