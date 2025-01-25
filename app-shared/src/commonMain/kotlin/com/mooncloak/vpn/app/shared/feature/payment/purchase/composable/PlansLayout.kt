package com.mooncloak.vpn.app.shared.feature.payment.purchase.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.payment_action_select
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlansLayout(
    selectedPlan: Plan?,
    plans: List<Plan>,
    acceptedTerms: Boolean,
    loading: Boolean,
    noticeText: String?,
    termsAndConditionsText: AnnotatedString,
    onPlanSelected: (plan: Plan) -> Unit,
    onAcceptedTermsToggled: (accepted: Boolean) -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!loading && plans.isNotEmpty()) {
            items(
                items = plans,
                key = { plan -> plan.id },
                contentType = { "PlanCard" }
            ) { plan ->
                PlanCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = plan.title,
                    description = plan.description,
                    price = plan.price.formatted ?: "", // TODO: Format price
                    highlight = plan.highlight,
                    selected = selectedPlan == plan,
                    enabled = plan.active,
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
                    enabled = selectedPlan != null && acceptedTerms,
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
