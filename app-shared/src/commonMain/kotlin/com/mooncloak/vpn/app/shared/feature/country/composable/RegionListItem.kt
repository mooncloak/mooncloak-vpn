package com.mooncloak.vpn.app.shared.feature.country.composable

import com.mooncloak.vpn.app.shared.api.location.Region
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun RegionListItem(
    region: Region,
    onMoreSelected: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    ListItem(
        modifier = modifier,
        leadingContent = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.PinDrop,
                contentDescription = null,
                tint = contentColor
            )
        },
        headlineContent = {
            Text(text = region.name)
        },
        trailingContent = {
            Icon(
                modifier = Modifier.size(36.dp)
                    .clip(CircleShape)
                    .clickable { onMoreSelected.invoke() }
                    .padding(8.dp),
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                tint = contentColor
            )
        }
    )
}
