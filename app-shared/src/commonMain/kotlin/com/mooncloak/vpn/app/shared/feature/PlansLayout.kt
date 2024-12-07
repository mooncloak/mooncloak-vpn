package com.mooncloak.vpn.app.shared.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
internal fun PlansLayout(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        PlanCard(
            modifier = Modifier.padding(16.dp),
            title = "Day",
            description = "Valid for one day or 1Gb of usage. Whichever comes first.",
            price = "$3",
            highlight = null,
            selected = false,
            onSelected = {

            }
        )

        PlanCard(
            modifier = Modifier.padding(16.dp),
            title = "Week",
            description = "Valid for one week or 10Gb of usage. Whichever comes first.",
            price = "$5",
            highlight = "Most Popular",
            selected = true,
            onSelected = {

            }
        )

        PlanCard(
            modifier = Modifier.padding(16.dp),
            title = "Month",
            description = "Valid for one month or 50Gb of usage. Whichever comes first.",
            price = "$8",
            highlight = null,
            selected = false,
            onSelected = {

            }
        )
    }
}

@Composable
internal fun PlanCard(
    title: String,
    description: String,
    price: String,
    highlight: String? = null,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedCard(
        modifier = modifier,
        onClick = onSelected,
        enabled = enabled,
        colors = CardDefaults.outlinedCardColors(),
        border = BorderStroke(
            width = 2.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Clip,
                        textAlign = TextAlign.Start
                    )

                    Text(
                        text = price,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (highlight != null) {
                    SuggestionChip(
                        onClick = {},
                        enabled = false,
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            labelColor = MaterialTheme.colorScheme.onSecondary,
                            disabledContainerColor = MaterialTheme.colorScheme.secondary,
                            disabledLabelColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        label = {
                            Text(text = highlight)
                        }
                    )
                }
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(color = LocalContentColor.current.copy(alpha = 0.68f))
            )
        }
    }
}
