package com.mooncloak.vpn.app.shared.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun BottomSheetHeader(
    title: String,
    description: String? = null,
    modifier: Modifier = Modifier,
    includeHorizontalPadding: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = if (includeHorizontalPadding) 16.dp else 0.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        if (!description.isNullOrBlank()) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = if (includeHorizontalPadding) 16.dp else 0.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                )
            )
        }
    }
}
