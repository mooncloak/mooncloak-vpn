package com.mooncloak.vpn.app.shared.feature.payment.purchase.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_label_one_time_charge
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.theme.onPrimaryVariant
import com.mooncloak.vpn.app.shared.theme.primaryVariant
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlanCard(
    title: String,
    description: String?,
    price: String,
    highlight: String? = null,
    selected: Boolean,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isInDarkMode: Boolean = isSystemInDarkTheme(),
    accentColor: Color = MaterialTheme.colorScheme.tertiary,
    onAccentColor: Color = MaterialTheme.colorScheme.onTertiary
) {
    Card(
        modifier = modifier,
        onClick = onSelected,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryVariant(isInDarkMode = isInDarkMode)
            } else {
                MaterialTheme.colorScheme.background
            },
            contentColor = if (selected) {
                MaterialTheme.colorScheme.onPrimaryVariant(isInDarkMode = isInDarkMode)
            } else {
                MaterialTheme.colorScheme.onBackground
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.background(
                    color = accentColor,
                    shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 12.dp)
                ).wrapContentSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    lineBreak = LineBreak.Heading
                ),
                color = onAccentColor,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )

            Column(
                modifier = Modifier.padding(horizontal = 12.dp)
                    .padding(top = 16.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier,
                            text = price,
                            style = MaterialTheme.typography.headlineLarge,
                            color = LocalContentColor.current
                        )

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(Res.string.payment_label_one_time_charge),
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = SecondaryAlpha)
                        )
                    }

                    if (!highlight.isNullOrBlank()) {
                        SuggestionChip(
                            modifier = Modifier.padding(start = 16.dp)
                                .wrapContentSize(),
                            onClick = {},
                            enabled = false,
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = accentColor,
                                labelColor = onAccentColor,
                                disabledContainerColor = accentColor,
                                disabledLabelColor = onAccentColor
                            ),
                            label = {
                                Text(text = highlight)
                            }
                        )
                    }
                }

                if (description != null) {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineBreak = LineBreak.Paragraph
                        ),
                        color = LocalContentColor.current.copy(alpha = SecondaryAlpha)
                    )
                }
            }
        }
    }
}
