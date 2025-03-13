package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShieldMoon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_not_available
import com.mooncloak.vpn.app.shared.resource.moon_shield_active
import com.mooncloak.vpn.app.shared.resource.moon_shield_description_brief
import com.mooncloak.vpn.app.shared.resource.moon_shield_inactive
import com.mooncloak.vpn.app.shared.resource.moon_shield_label_data_saved
import com.mooncloak.vpn.app.shared.resource.moon_shield_label_time_saved
import com.mooncloak.vpn.app.shared.resource.moon_shield_label_trackers_blocked
import com.mooncloak.vpn.app.shared.resource.moon_shield_title
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.theme.TertiaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MoonShieldCard(
    active: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
    onActiveChange: (active: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    trackersBlocked: String? = null,
    timeSaved: String? = null,
    bytesSaved: String? = null
) {
    val backgroundColor = animateColorAsState(
        targetValue = if (active) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.error
        }
    )
    val contentColor = animateColorAsState(
        targetValue = if (active) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onError
        }
    )

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.value,
            contentColor = contentColor.value
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            HomeCardHeader(
                modifier = Modifier.fillMaxWidth(),
                leadingText = stringResource(Res.string.moon_shield_title),
                leadingIcon = Icons.Default.ShieldMoon,
                trailingIcon = Icons.Default.Info,
                trailingIconSize = 18.dp,
                leadingContentColor = contentColor.value,
                trailingContentColor = contentColor.value.copy(alpha = SecondaryAlpha)
            )

            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp),
                text = stringResource(Res.string.moon_shield_description_brief),
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor.value.copy(alpha = SecondaryAlpha)
            )

            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (active) {
                        stringResource(Res.string.moon_shield_active)
                    } else {
                        stringResource(Res.string.moon_shield_inactive)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor.value
                )

                Switch(
                    enabled = enabled,
                    checked = active,
                    onCheckedChange = onActiveChange,
                    colors = SwitchDefaults.colors()
                )
            }

            if (!trackersBlocked.isNullOrBlank() || !timeSaved.isNullOrBlank() || !bytesSaved.isNullOrBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InfoStatSection(
                        modifier = Modifier.weight(1f)
                            .padding(horizontal = 8.dp),
                        label = stringResource(Res.string.moon_shield_label_trackers_blocked),
                        value = trackersBlocked ?: stringResource(Res.string.global_not_available)
                    )

                    VerticalDivider(
                        color = contentColor.value.copy(alpha = TertiaryAlpha)
                    )

                    InfoStatSection(
                        modifier = Modifier.weight(1f)
                            .padding(horizontal = 8.dp),
                        label = stringResource(Res.string.moon_shield_label_time_saved),
                        value = timeSaved ?: stringResource(Res.string.global_not_available)
                    )

                    VerticalDivider(
                        color = contentColor.value.copy(alpha = TertiaryAlpha)
                    )

                    InfoStatSection(
                        modifier = Modifier.weight(1f)
                            .padding(horizontal = 8.dp),
                        label = stringResource(Res.string.moon_shield_label_data_saved),
                        value = bytesSaved ?: stringResource(Res.string.global_not_available)
                    )
                }
            }
        }
    }
}
