package com.mooncloak.vpn.app.shared.feature.home.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
internal fun InfoStatSection(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.68f)),
            maxLines = 2,
            overflow = TextOverflow.Clip,
            textAlign = TextAlign.Center
        )
    }
}
