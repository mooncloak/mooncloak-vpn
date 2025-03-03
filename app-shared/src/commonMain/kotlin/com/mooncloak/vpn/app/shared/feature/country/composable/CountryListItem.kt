package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.vpn.app.shared.composable.FlagImage
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_options_more
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CountryListItem(
    country: Country,
    onMoreSelected: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    invertedContainerColor: Color = MaterialTheme.colorScheme.background,
    invertedContentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    ListItem(
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = containerColor
        ),
        leadingContent = {
            FlagImage(
                modifier = Modifier.width(36.dp),
                imageUri = country.flag,
                containerColor = invertedContainerColor,
                contentColor = invertedContentColor
            )
        },
        headlineContent = {
            Text(
                text = country.name ?: ""
            )
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
