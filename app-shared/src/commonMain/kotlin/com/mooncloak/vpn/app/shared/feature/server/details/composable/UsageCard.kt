package com.mooncloak.vpn.app.shared.feature.server.details.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.server_details_label_download
import com.mooncloak.vpn.app.shared.resource.server_details_label_upload
import com.mooncloak.vpn.app.shared.resource.server_details_title_data_usage
import com.mooncloak.vpn.app.shared.util.DataFormatter
import com.mooncloak.vpn.app.shared.util.Default
import com.mooncloak.vpn.app.shared.util.formatWithUnit
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun UsageCard(
    downloadBytes: Long?,
    uploadBytes: Long?,
    modifier: Modifier = Modifier,
    formatter: DataFormatter = remember { DataFormatter.Default }
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
            ServerConnectionCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.server_details_title_data_usage),
                leadingIcon = Icons.Default.DataUsage
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(top = 16.dp)
            ) {
                StatContent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.server_details_label_download),
                    value = downloadBytes?.let { download ->
                        formatter.formatWithUnit(
                            value = download,
                            inputType = DataFormatter.Type.Bytes,
                            outputType = DataFormatter.Type.Megabytes
                        )
                    } ?: stringResource(Res.string.global_not_available),
                    icon = Icons.Default.Download
                )

                VerticalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                StatContent(
                    modifier = Modifier.weight(1f),
                    label = stringResource(Res.string.server_details_label_upload),
                    value = uploadBytes?.let { upload ->
                        formatter.formatWithUnit(
                            value = upload,
                            inputType = DataFormatter.Type.Bytes,
                            outputType = DataFormatter.Type.Megabytes
                        )
                    } ?: stringResource(Res.string.global_not_available),
                    icon = Icons.Default.Upload
                )
            }
        }
    }
}
