package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mooncloak.vpn.app.shared.api.Country

@Composable
internal fun CountryListItem(
    country: Country,
    onMoreSelected: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    ListItem(
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        leadingContent = {
            Box(
                modifier = Modifier.width(36.dp)
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                val imageUri = country.flag

                if (imageUri != null) {
                    AsyncImage(
                        modifier = Modifier.matchParentSize(),
                        model = imageUri,
                        contentDescription = null
                    )
                } else {
                    Icon(
                        modifier = Modifier.size(24.dp)
                            .align(Alignment.Center),
                        imageVector = Icons.Default.Flag,
                        contentDescription = null,
                        tint = contentColor
                    )
                }
            }
        },
        headlineContent = {
            Text(
                text = country.name
            )
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
