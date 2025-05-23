package com.mooncloak.vpn.app.shared.feature.country.composable

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
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.locale.Region
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_options_more
import com.mooncloak.vpn.app.shared.resource.global_not_available
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RegionListItem(
    country: Country,
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
            Text(text = region.name ?: country.name ?: stringResource(Res.string.global_not_available))
        },
        trailingContent = {
            Icon(
                modifier = Modifier.size(36.dp)
                    .clip(CircleShape)
                    .clickable { onMoreSelected.invoke() }
                    .padding(8.dp),
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = stringResource(Res.string.cd_options_more),
                tint = contentColor
            )
        }
    )
}
