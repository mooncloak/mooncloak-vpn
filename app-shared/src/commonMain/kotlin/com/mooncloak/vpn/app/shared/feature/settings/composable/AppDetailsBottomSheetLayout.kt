package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.DetailRow
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.feature.settings.model.SettingsAppDetails
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_no
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.global_yes
import com.mooncloak.vpn.app.shared.resource.settings_app_details_description
import com.mooncloak.vpn.app.shared.resource.settings_app_details_header
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_build_time
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_debug
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_id
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_name
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_pre_release
import com.mooncloak.vpn.app.shared.resource.settings_app_details_title_version
import com.mooncloak.vpn.util.shared.time.DateTimeFormatter
import com.mooncloak.vpn.util.shared.time.Full
import com.mooncloak.vpn.util.shared.time.format
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AppDetailsBottomSheetLayout(
    sheetState: ManagedModalBottomSheetState,
    details: SettingsAppDetails?,
    modifier: Modifier = Modifier,
    dateTimeFormatter: DateTimeFormatter = remember { DateTimeFormatter.Full }
) {
    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.settings_app_details_header),
            description = stringResource(Res.string.settings_app_details_description)
        ) {
            AppInfoCard(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                id = details?.id,
                name = details?.name,
                version = details?.version,
                debug = if (details?.isDebug == true) {
                    stringResource(Res.string.global_yes)
                } else {
                    stringResource(Res.string.global_no)
                },
                preRelease = if (details?.isPreRelease == true) {
                    stringResource(Res.string.global_yes)
                } else {
                    stringResource(Res.string.global_no)
                },
                buildTime = details?.buildTime?.let { dateTimeFormatter.format(it) }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
internal fun AppInfoCard(
    id: String?,
    name: String?,
    version: String?,
    debug: String?,
    preRelease: String?,
    buildTime: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.settings_app_details_title_id),
                value = id?.takeIf { it.isNotBlank() } ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.settings_app_details_title_name),
                value = name?.takeIf { it.isNotBlank() } ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.settings_app_details_title_version),
                value = version?.takeIf { it.isNotBlank() } ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.settings_app_details_title_debug),
                value = debug?.takeIf { it.isNotBlank() } ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.settings_app_details_title_pre_release),
                value = preRelease?.takeIf { it.isNotBlank() } ?: stringResource(Res.string.global_not_available)
            )

            DetailRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = stringResource(Res.string.settings_app_details_title_build_time),
                value = buildTime.takeIf { !it.isNullOrBlank() } ?: stringResource(Res.string.global_not_available)
            )
        }
    }
}
