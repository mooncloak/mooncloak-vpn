package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.billing.ServicePurchaseReceiptRepository
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceSubscriptionUsage
import com.mooncloak.vpn.api.shared.service.isActive
import com.mooncloak.vpn.api.shared.token.Token
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.subscription.model.SubscriptionDetails
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.resource.subscription_error_load_details
import com.mooncloak.vpn.app.shared.resource.subscription_error_load_last_payment
import com.mooncloak.vpn.app.shared.resource.subscription_error_load_plan
import com.mooncloak.vpn.app.shared.resource.subscription_error_load_usage
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings
import com.mooncloak.vpn.app.shared.util.DataFormatter
import com.mooncloak.vpn.app.shared.util.Default
import com.mooncloak.vpn.app.shared.util.time.remaining
import com.mooncloak.vpn.util.shared.time.DateTimeFormatter
import com.mooncloak.vpn.util.shared.time.DurationFormatter
import com.mooncloak.vpn.util.shared.time.Full
import com.mooncloak.vpn.util.shared.time.format
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class SubscriptionViewModel @Inject public constructor(
    private val subscriptionStorage: SubscriptionSettings,
    private val serviceTokensRepository: ServiceTokensRepository,
    private val api: VpnServiceApi,
    private val plansRepository: ServicePlansRepository,
    private val purchaseReceiptRepository: ServicePurchaseReceiptRepository,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.Full,
    private val durationFormatter: DurationFormatter = DurationFormatter.remaining(),
    private val dataFormatter: DataFormatter = DataFormatter.Default,
    private val clock: Clock
) : ViewModel<SubscriptionStateModel>(initialStateValue = SubscriptionStateModel()) {

    public fun load() {
        coroutineScope.launch {
            emit { current -> current.copy(isLoading = true) }

            try {
                val token = serviceTokensRepository.getLatest()?.accessToken
                val subscription = subscriptionStorage.subscription.get()

                // Let's emit the subscription first so that the screen UI loads faster.
                emit { current -> current.copy(subscription = subscription) }

                // Just emit the data as you get it, so the screen is more dynamic.
                launch {
                    loadDetails(
                        subscription = subscription,
                        usage = state.current.value.usage
                    )
                }
                launch { loadUsage(token = token) }
                launch { loadPlan(planId = subscription?.planId) }
                launch { loadLastReceipt() }

                emit { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                LogPile.error(message = "Error loading subscription.", cause = e)

                emit { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                }
            }
        }
    }

    private suspend fun loadDetails(
        subscription: ServiceSubscription?,
        usage: ServiceSubscriptionUsage? = null
    ) {
        try {
            if (subscription != null && subscription.isActive(clock.now())) {
                val purchased = dateTimeFormatter.format(subscription.created)
                val expiration = dateTimeFormatter.format(subscription.expiration)
                val totalData = (subscription.totalThroughput)?.let { bytes ->
                    dataFormatter.format(
                        value = bytes,
                        inputType = DataFormatter.Type.Bytes,
                        outputType = DataFormatter.Type.Megabytes
                    )
                }
                val remainingDuration = durationFormatter.format(
                    usage?.durationRemaining ?: (subscription.expiration - clock.now())
                )
                val remainingData = usage?.totalThroughputRemaining?.let { bytes ->
                    dataFormatter.format(
                        value = bytes,
                        inputType = DataFormatter.Type.Bytes,
                        outputType = DataFormatter.Type.Megabytes
                    )
                }

                emit { current ->
                    current.copy(
                        details = SubscriptionDetails(
                            purchased = purchased,
                            expiration = expiration,
                            totalData = totalData,
                            remainingDuration = remainingDuration,
                            remainingData = remainingData
                        )
                    )
                }
            } else {
                emit { current ->
                    current.copy(details = null)
                }
            }
        } catch (e: Exception) {
            LogPile.error(
                tag = TAG,
                message = "Error loading subscription details.",
                cause = e
            )

            emit { current ->
                current.copy(
                    errorMessage = getString(Res.string.subscription_error_load_details)
                )
            }
        }
    }

    private suspend fun loadUsage(token: Token?) {
        try {
            if (token != null) {
                val usage = api.getCurrentSubscriptionUsage(token = token)

                emit { current ->
                    current.copy(usage = usage)
                }

                loadDetails(
                    subscription = state.current.value.subscription,
                    usage = usage
                )
            }
        } catch (e: Exception) {
            LogPile.error(
                tag = TAG,
                message = "Error loading subscription usage.",
                cause = e
            )

            emit { current ->
                current.copy(
                    errorMessage = getString(Res.string.subscription_error_load_usage)
                )
            }
        }
    }

    private suspend fun loadPlan(planId: String?) {
        try {
            if (planId != null) {
                val plan = runCatching {
                    withContext(Dispatchers.IO) {
                        plansRepository.getPlan(id = planId)
                    }
                }.getOrNull()

                emit { current ->
                    current.copy(
                        plan = plan
                    )
                }
            }
        } catch (e: Exception) {
            LogPile.error(
                tag = TAG,
                message = "Error loading subscription plan.",
                cause = e
            )

            emit { current ->
                current.copy(
                    errorMessage = getString(Res.string.subscription_error_load_plan)
                )
            }
        }
    }

    private suspend fun loadLastReceipt() {
        try {
            val receipt = purchaseReceiptRepository.getLatest()

            emit { current ->
                current.copy(lastReceipt = receipt)
            }
        } catch (e: Exception) {
            LogPile.error(
                tag = TAG,
                message = "Error loading last subscription payment receipt.",
                cause = e
            )

            emit { current ->
                current.copy(
                    errorMessage = getString(Res.string.subscription_error_load_last_payment)
                )
            }
        }
    }

    public companion object {

        private const val TAG: String = "SubscriptionViewModel"
    }
}
