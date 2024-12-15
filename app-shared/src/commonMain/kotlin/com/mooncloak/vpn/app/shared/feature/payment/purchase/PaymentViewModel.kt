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
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.BitcoinPlanInvoice
import com.mooncloak.vpn.app.shared.api.Currency
import com.mooncloak.vpn.app.shared.api.CurrencyType
import com.mooncloak.vpn.app.shared.api.Plan
import com.mooncloak.vpn.app.shared.api.PlanPaymentStatus
import com.mooncloak.vpn.app.shared.api.Price
import com.mooncloak.vpn.app.shared.api.TransactionToken
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.info.AppClientInfo
import com.mooncloak.vpn.app.shared.feature.payment.purchase.model.PaymentDestination
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.payment_accept_terms_and_conditions
import com.mooncloak.vpn.app.shared.resource.payment_link_text_privacy_policy
import com.mooncloak.vpn.app.shared.resource.payment_link_text_terms
import com.mooncloak.vpn.app.shared.resource.payment_plans_title
import com.mooncloak.vpn.app.shared.resource.payment_title
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString

@Stable
@ComponentScoped
public class PaymentViewModel @Inject public constructor(
    private val appClientInfo: AppClientInfo,
    private val navController: NavController
) : ViewModel<PaymentStateModel>(initialStateValue = PaymentStateModel()) {

    private val testPlans = listOf(
        Plan(
            id = "1",
            title = "One Day",
            description = "Valid for one day or 1Gb of usage. Whichever comes first.",
            price = Price(
                currency = Currency(type = CurrencyType.Iso4217, code = "USD"),
                amount = 3,
                formatted = "$3"
            ),
            created = Clock.System.now()
        ),
        Plan(
            id = "2",
            title = "One Week",
            description = "Valid for one week or 10Gb of usage. Whichever comes first.",
            price = Price(
                currency = Currency(type = CurrencyType.Iso4217, code = "USD"),
                amount = 5,
                formatted = "$5"
            ),
            highlight = "Most Popular",
            created = Clock.System.now()
        ),
        Plan(
            id = "3",
            title = "One Month",
            description = "Valid for one month or 100Gb of usage. Whichever comes first.",
            price = Price(
                currency = Currency(type = CurrencyType.Iso4217, code = "USD"),
                amount = 8,
                formatted = "$8"
            ),
            created = Clock.System.now()
        )
    )

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                emit(value = state.current.value.copy(isLoading = true))

                var termsAndConditionsText: (@Composable () -> AnnotatedString) = { AnnotatedString("") }

                try {
                    termsAndConditionsText = getTermsAndConditionsText()

                    // TODO: Load real plans
                    // TODO: Obtain current plan invoice
                    // TODO: Use presence of plan invoice to determine whether to show plans or invoice screen

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            plans = testPlans,
                            termsAndConditionsText = termsAndConditionsText,
                            startDestination = PaymentDestination.Plans, // TODO:
                            screenTitle = getString(Res.string.payment_plans_title)
                        )
                    )
                } catch (e: Exception) {
                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = e.message ?: getString(Res.string.global_unexpected_error),
                            termsAndConditionsText = termsAndConditionsText,
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
                    emit(value = state.current.value.copy(isLoading = true))

                    // TODO: Create new plan invoice

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = null,
                            invoice = BitcoinPlanInvoice( // TODO: Remove test plan data
                                id = "123",
                                planId = "123",
                                token = TransactionToken(value = "123"),
                                created = Clock.System.now(),
                                uri = "https://mooncloak.com/testinginvoice",
                                address = "123",
                                amount = Price(
                                    currency = Currency(
                                        type = CurrencyType.Crypto,
                                        code = "B"
                                    ),
                                    amount = 1,
                                    formatted = "B 0.00000000001"
                                )
                            ),
                            paymentStatus = PlanPaymentStatus.Pending(
                                invoiceId = "123",
                                title = "Pending Payment",
                                timestamp = Clock.System.now()
                            ),
                            screenTitle = getString(Res.string.payment_title)
                        )
                    )

                    navController.navigate(PaymentDestination.Invoice) {
                        popUpTo(PaymentDestination.Plans) {
                            inclusive = true
                        }
                    }
                } catch (e: Exception) {
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
