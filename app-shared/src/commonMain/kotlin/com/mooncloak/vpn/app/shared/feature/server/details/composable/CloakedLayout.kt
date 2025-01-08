package com.mooncloak.vpn.app.shared.feature.server.details.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.server_details_title_time
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CloakedLayout(
    since: Instant,
    modifier: Modifier = Modifier,
    multiplier: Float = 1f
) {
    SolarEclipseLayout(
        modifier = modifier,
        multiplier = multiplier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.server_details_title_time),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground.copy(
                        alpha = SecondaryAlpha
                    )
                )
            )

            TimerText(
                modifier = Modifier.wrapContentSize()
                    .padding(top = 12.dp),
                style = MaterialTheme.typography.titleLarge,
                since = since
            )
        }
    }
}
