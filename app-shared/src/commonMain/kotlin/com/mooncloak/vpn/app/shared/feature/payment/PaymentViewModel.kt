package com.mooncloak.vpn.app.shared.feature.payment

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.Currency
import com.mooncloak.vpn.app.shared.api.CurrencyType
import com.mooncloak.vpn.app.shared.api.Plan
import com.mooncloak.vpn.app.shared.api.Price
import com.mooncloak.vpn.app.shared.di.ComponentScoped
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString

@Stable
@ComponentScoped
public class PaymentViewModel @Inject public constructor(

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

    public fun load() {
        emit(value = state.current.value.copy(isLoading = true))

        coroutineScope.launch {
            try {
                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        errorMessage = null,
                        plans = testPlans
                    )
                )
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

    public fun selectPlan(plan: Plan) {
        emit(
            value = state.current.value.copy(
                selectedPlan = plan
            )
        )
    }

    public fun toggleAcceptTerms(accepted: Boolean) {
        emit(
            value = state.current.value.copy(
                acceptedTerms = accepted
            )
        )
    }
}
