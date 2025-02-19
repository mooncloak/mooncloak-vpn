package com.mooncloak.vpn.app.shared.feature.subscription

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.plan.Plan
import com.mooncloak.vpn.app.shared.api.plan.ServicePlansRepository
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscriptionUsage
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getString

@Stable
@FeatureScoped
public class SubscriptionViewModel @Inject public constructor(
    private val subscriptionStorage: SubscriptionStorage,
    private val api: MooncloakVpnServiceHttpApi,
    private val plansRepository: ServicePlansRepository
) : ViewModel<SubscriptionStateModel>(initialStateValue = SubscriptionStateModel()) {

    @OptIn(ExperimentalPersistentStateAPI::class)
    public fun load() {
        // TODO:
        coroutineScope.launch {
            emit { current -> current.copy(isLoading = true) }

            var subscription: ServiceSubscription? = null
            var usage: ServiceSubscriptionUsage? = null
            var plan: Plan? = null

            try {
                val token = subscriptionStorage.tokens.current.value?.accessToken
                subscription = subscriptionStorage.subscription.current.value

                // Let's emit the subscription first so that the screen UI loads faster.
                emit { current -> current.copy(subscription = subscription) }

                if (token != null) {
                    usage = withContext(Dispatchers.IO) {
                        api.getCurrentSubscriptionUsage(token = token)
                    }
                }

                subscription?.planId?.let { planId ->
                    plan = runCatching {
                        withContext(Dispatchers.IO) {
                            plansRepository.getPlan(id = planId)
                        }
                    }.getOrNull()
                }

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        subscription = subscription,
                        usage = usage,
                        plan = plan,
                        errorMessage = null
                    )
                )
            } catch (e: Exception) {
                LogPile.error(message = "Error loading subscription details.", cause = e)

                emit(
                    value = state.current.value.copy(
                        isLoading = false,
                        subscription = subscription,
                        usage = usage,
                        plan = plan,
                        errorMessage = getString(Res.string.global_unexpected_error)
                    )
                )
            }
        }
    }
}
