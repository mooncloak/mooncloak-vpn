package com.mooncloak.vpn.app.shared.feature.support.composable

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.support_faq_header
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FAQHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            imageVector = Icons.Default.Quiz,
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(start = 16.dp)
                .weight(1f),
            text = stringResource(Res.string.support_faq_header),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
