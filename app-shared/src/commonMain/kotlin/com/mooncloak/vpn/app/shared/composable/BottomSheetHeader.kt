package com.mooncloak.vpn.app.shared.composable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha

@Composable
internal fun BottomSheetHeader(
    title: String? = null,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    includeHorizontalPadding: Boolean = true
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.padding(horizontal = if (includeHorizontalPadding) 16.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.wrapContentSize()
                    .animateContentSize()
            ) {
                icon?.invoke()
            }

            if (!title.isNullOrBlank()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start
                )
            }
        }

        if (!description.isNullOrBlank()) {
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 8.dp)
                    .padding(horizontal = if (includeHorizontalPadding) 16.dp else 0.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                textAlign = TextAlign.Start
            )
        }
    }
}
