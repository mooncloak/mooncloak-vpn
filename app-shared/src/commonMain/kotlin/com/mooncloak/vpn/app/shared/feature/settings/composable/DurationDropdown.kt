package com.mooncloak.vpn.app.shared.feature.settings.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Surface
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import kotlin.time.Duration
import kotlin.time.DurationUnit

@Composable
internal fun DurationDropdown(
    value: Duration,
    durations: Set<Duration>,
    onValueChanged: (duration: Duration) -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    durationUnit: DurationUnit = DurationUnit.MINUTES,
    decimals: Int = 0,
    text: (duration: Duration) -> String = { duration -> duration.toString(unit = durationUnit, decimals = decimals) },
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        modifier = modifier.background(MaterialTheme.colorScheme.surface)
            .clip(MaterialTheme.shapes.medium),
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column {
                durations.forEach { duration ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = text(duration)
                            )
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = if (value == duration) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        ),
                        onClick = {
                            onValueChanged(duration)
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}
