package com.mooncloak.vpn.app.shared.feature.payment.purchase.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.AllPlansCard
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.NoPlansCard
import com.mooncloak.vpn.app.shared.feature.payment.purchase.composable.PlanCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_action_select
import com.mooncloak.vpn.app.shared.theme.ColorPalette
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import com.mooncloak.vpn.app.shared.util.format
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlansLayout(
    selectedPlan: Plan?,
    plans: List<Plan>,
    acceptedTerms: Boolean,
    loading: Boolean,
    purchasing: Boolean,
    noticeText: String?,
    termsAndConditionsText: AnnotatedString,
    isInDarkMode: Boolean,
    onPlanSelected: (plan: Plan) -> Unit,
    onAcceptedTermsToggled: (accepted: Boolean) -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColors = remember {
        listOf(
            ColorPalette.Blue_500 to Color.White,
            ColorPalette.Teal_500 to Color.White,
            ColorPalette.Purple_600 to Color.White
        )
    }

    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(
            key = "AllPlansCard"
        ) {
            AllPlansCard(
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (!loading && plans.isNotEmpty()) {
            itemsIndexed(
                items = plans,
                key = { _, plan -> plan.id },
                contentType = { _, _ -> "PlanCard" }
            ) { index, plan ->
                PlanCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = plan.title,
                    description = plan.description?.value,
                    price = plan.price.format() ?: "",
                    highlight = plan.highlight,
                    selected = selectedPlan == plan,
                    enabled = plan.active,
                    isInDarkMode = isInDarkMode,
                    accentColor = accentColors[index % accentColors.size].first,
                    onAccentColor = accentColors[index % accentColors.size].second,
                    onSelected = {
                        onPlanSelected.invoke(plan)
                    }
                )
            }

            item(key = "TermsAndConditions") {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (noticeText != null) {
                        Text(
                            text = noticeText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = SecondaryAlpha
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = if (noticeText != null) 32.dp else 0.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = acceptedTerms,
                            onClick = {
                                onAcceptedTermsToggled.invoke(!acceptedTerms)
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = termsAndConditionsText
                        )
                    }
                }
            }

            item(key = "Select") {
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    enabled = selectedPlan != null && acceptedTerms && !purchasing,
                    onClick = onSelect
                ) {
                    Text(text = stringResource(Res.string.payment_action_select))
                }
            }
        } else if (!loading) {
            item(key = "NoPlanDataAvailable") {
                NoPlansCard(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
