package com.mooncloak.vpn.app.shared.feature.country.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.location.CountryWithVPNService
import com.mooncloak.vpn.app.shared.api.location.Region
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.composable.FlagImage
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheet
import com.mooncloak.vpn.app.shared.composable.ModalNavigationBottomSheetState
import com.mooncloak.vpn.app.shared.feature.country.model.CountryListBottomSheetDestination
import com.mooncloak.vpn.app.shared.feature.server.region.RegionServerListScreen
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_action_back
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CountryListBottomSheet(
    state: ModalNavigationBottomSheetState<CountryListBottomSheetDestination>,
    onConnectToRegion: (region: Region) -> Unit,
    onConnectToServer: (server: Server) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationBottomSheet(
        state = state,
        modifier = modifier
    ) { destination ->
        Column(modifier = Modifier.fillMaxWidth()) {
            CountryListBottomSheetHeader(
                modifier = Modifier.fillMaxWidth(),
                title = when (destination) {
                    is CountryListBottomSheetDestination.RegionList -> destination.country.name
                    is CountryListBottomSheetDestination.ServerList -> destination.country.name
                },
                supporting = when (destination) {
                    is CountryListBottomSheetDestination.RegionList -> null
                    is CountryListBottomSheetDestination.ServerList -> destination.region.name
                },
                description = when (destination) {
                    is CountryListBottomSheetDestination.RegionList -> (destination.country as? CountryWithVPNService)?.connectionDescription
                    is CountryListBottomSheetDestination.ServerList -> (destination.country as? CountryWithVPNService)?.connectionDescription
                },
                imageUri = when (destination) {
                    is CountryListBottomSheetDestination.RegionList -> destination.country.flag
                    is CountryListBottomSheetDestination.ServerList -> destination.country.flag
                },
                onBack = when (destination) {
                    is CountryListBottomSheetDestination.RegionList -> null
                    is CountryListBottomSheetDestination.ServerList -> ({
                        coroutineScope.launch {
                            state.show(
                                CountryListBottomSheetDestination.RegionList(
                                    country = destination.country
                                )
                            )
                        }
                    })
                }
            )

            when (destination) {
                is CountryListBottomSheetDestination.RegionList -> RegionListLayout(
                    modifier = Modifier.fillMaxWidth(),
                    country = destination.country,
                    onRegionDetails = { region ->
                        coroutineScope.launch {
                            state.show(
                                destination = CountryListBottomSheetDestination.ServerList(
                                    country = destination.country,
                                    region = region
                                )
                            )
                        }
                    },
                    onConnect = onConnectToRegion
                )

                is CountryListBottomSheetDestination.ServerList -> RegionServerListScreen(
                    modifier = Modifier.fillMaxWidth(),
                    country = destination.country,
                    region = destination.region,
                    onConnect = onConnectToServer
                )
            }
        }
    }
}

@Composable
private fun CountryListBottomSheetHeader(
    title: String,
    supporting: String? = null,
    description: String? = null,
    imageUri: String? = null,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ListItem(
            leadingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        visible = onBack != null
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { onBack?.invoke() }
                                .padding(8.dp),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(Res.string.cd_action_back),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    FlagImage(
                        modifier = Modifier.width(36.dp),
                        imageUri = imageUri
                    )
                }
            },
            headlineContent = {
                Text(text = title)
            },
            supportingContent = (@Composable {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = supporting ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                )
            }).takeIf { supporting != null }
        )

        if (description != null) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )
            )
        }
    }
}
