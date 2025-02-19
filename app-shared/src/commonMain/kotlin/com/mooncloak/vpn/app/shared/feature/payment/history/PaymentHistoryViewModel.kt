package com.mooncloak.vpn.app.shared.feature.payment.history

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.app.shared.api.billing.ServicePurchaseReceiptRepository
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class PaymentHistoryViewModel @Inject public constructor(
    private val servicePurchaseReceiptRepository: ServicePurchaseReceiptRepository,
    private val plansRepository: ServicePlansRepository
) : ViewModel<PaymentHistoryStateModel>(initialStateValue = PaymentHistoryStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    emit(
                        value = state.current.value.copy(
                            isLoading = true
                        )
                    )

                    // TODO: Support pagination
                    val receipts = servicePurchaseReceiptRepository.getPage()

                    emit(
                        value = state.current.value.copy(
                            receipts = receipts,
                            isLoading = false,
                            errorMessage = null
                        )
                    )

                    // We move this to after the receipts are first loaded so that the first load is fast, and the update
                    // can come as needed.
                    val plansMap = plansRepository.getPlans().associateBy { plan -> plan.id }
                    val updatedReceipts = receipts.map { receipt ->
                        val price = receipt.price
                            ?: receipt.planIds.firstNotNullOfOrNull { planId ->
                                plansMap[planId]
                            }?.price

                        receipt.copy(price = price)
                    }

                    println("Payment: plansMap: $plansMap")
                    println("Payment: receipts: $receipts")
                    println("Payment: updateReceipts: $updatedReceipts")

                    if (receipts != updatedReceipts) {
                        emit(
                            value = state.current.value.copy(
                                receipts = updatedReceipts,
                                isLoading = false,
                                errorMessage = null
                            )
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error loading transaction history.",
                        cause = e
                    )

                    emit(
                        value = state.current.value.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    )
                }
            }
        }
    }
}
