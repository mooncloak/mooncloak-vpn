package com.mooncloak.vpn.app.shared.feature.payment.purchase

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.billing.BillingManager
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansProvider
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.feature.payment.purchase.model.PaymentDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.payment_accept_terms_and_conditions
import com.mooncloak.vpn.app.shared.resource.payment_link_text_privacy_policy
import com.mooncloak.vpn.app.shared.resource.payment_link_text_terms
import com.mooncloak.vpn.app.shared.resource.payment_notice
import com.mooncloak.vpn.app.shared.resource.payment_notice_beta
import com.mooncloak.vpn.app.shared.resource.payment_plans_title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class PaymentViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val navController: NavController,
    private val plansProvider: ServicePlansProvider,
    private val billingManager: BillingManager
) : ViewModel<PaymentStateModel>(initialStateValue = PaymentStateModel()) {

    private val mutex = Mutex(locked = false)

    private var plansJob: Job? = null

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                emit(value = state.current.value.copy(isLoading = true))

                subscribeToPlans()

                var termsAndConditionsText: (@Composable () -> AnnotatedString) = { AnnotatedString("") }
                var noticeText = if (appClientInfo.isPreRelease) {
                    getString(Res.string.payment_notice_beta)
                } else {
                    getString(Res.string.payment_notice)
                }

                try {
                    termsAndConditionsText = getTermsAndConditionsText()

                    // TODO: Load real plans
                    // TODO: Obtain current plan invoice
                    // TODO: Use presence of plan invoice to determine whether to show plans or invoice screen

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            termsAndConditionsText = termsAndConditionsText,
                            noticeText = noticeText,
                            startDestination = PaymentDestination.Plans, // TODO:
                            screenTitle = getString(Res.string.payment_plans_title)
                        )
                    )
                } catch (e: Exception) {
                    LogPile.error(message = "Error loading data.", cause = e)

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = e.message ?: getString(Res.string.global_unexpected_error),
                            termsAndConditionsText = termsAndConditionsText,
                            noticeText = noticeText,
                            startDestination = PaymentDestination.Plans
                        )
                    )
                }
            }
        }
    }

    public fun selectPlan(plan: Plan) {
        coroutineScope.launch {
            mutex.withLock {
                emit(
                    value = state.current.value.copy(
                        selectedPlan = if (plan == state.current.value.selectedPlan) null else plan
                    )
                )
            }
        }
    }

    public fun toggleAcceptTerms(accepted: Boolean) {
        coroutineScope.launch {
            mutex.withLock {
                emit(
                    value = state.current.value.copy(
                        acceptedTerms = accepted
                    )
                )
            }
        }
    }

    public fun createInvoice() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    state.current.value.selectedPlan?.let { plan ->
                        billingManager.purchasePlan(plan)
                    }
                } catch (e: Exception) {
                    LogPile.error(message = "Error creating invoice.", cause = e)

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = e.message ?: getString(Res.string.global_unexpected_error)
                        )
                    )
                }
            }
        }
    }

    private fun subscribeToPlans() {
        plansJob?.cancel()

        plansJob = plansProvider.getPlansFlow()
            .flowOn(Dispatchers.IO)
            .onEach { plans -> emit { current -> current.copy(plans = plans) } }
            .catch { e -> LogPile.error(message = "Error retrieving plans.", cause = e) }
            .flowOn(Dispatchers.Main)
            .launchIn(coroutineScope)
    }

    private suspend fun getTermsAndConditionsText(): (@Composable () -> AnnotatedString) {
        val acceptTermsAndConditionsText = getString(Res.string.payment_accept_terms_and_conditions)
        val termsLinkText = getString(Res.string.payment_link_text_terms)
        val privacyPolicyLinkText = getString(Res.string.payment_link_text_privacy_policy)
        val indexOfTerms = acceptTermsAndConditionsText.indexOf(termsLinkText)
        val indexOfPrivacyPolicy = acceptTermsAndConditionsText.indexOf(privacyPolicyLinkText)

        return {
            AnnotatedString.Builder().apply {
                append(acceptTermsAndConditionsText)

                if (indexOfTerms != -1) {
                    addLink(
                        url = LinkAnnotation.Url(
                            url = appClientInfo.termsAndConditionsUri,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                pressedStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        ),
                        start = indexOfTerms,
                        end = indexOfTerms + termsLinkText.length
                    )
                }

                if (indexOfPrivacyPolicy != -1) {
                    addLink(
                        url = LinkAnnotation.Url(
                            url = appClientInfo.privacyPolicyUri,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                pressedStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        ),
                        start = indexOfPrivacyPolicy,
                        end = indexOfPrivacyPolicy + privacyPolicyLinkText.length
                    )
                }
            }.toAnnotatedString()
        }
    }
}
