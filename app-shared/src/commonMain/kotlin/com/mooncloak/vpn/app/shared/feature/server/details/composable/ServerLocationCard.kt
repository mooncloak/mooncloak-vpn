package com.mooncloak.vpn.app.shared.feature.server.details.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.FlagImage
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun ServerLocationCard(
    serverName: String,
    countryName: String? = null,
    regionName: String? = null,
    flagImageUri: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        val hasCountryName = !countryName.isNullOrBlank()
        val hasRegionName = !regionName.isNullOrBlank()

        ListItem(
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            leadingContent = {
                FlagImage(
                    modifier = Modifier.width(36.dp),
                    imageUri = flagImageUri
                )
            },
            headlineContent = {
                Text(
                    text = buildString {
                        if (hasCountryName) {
                            append(countryName)
                        }

                        if (hasRegionName) {
                            if (hasCountryName) {
                                append(" - $regionName")
                            } else {
                                append(regionName)
                            }
                        }

                        if (!hasCountryName && !hasRegionName) {
                            append(serverName)
                        }
                    }
                )
            },
            supportingContent = (@Composable {
                Text(
                    text = serverName,
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = SecondaryAlpha
                    )
                )
            }).takeIf { hasCountryName || hasRegionName }
        )
    }
}
