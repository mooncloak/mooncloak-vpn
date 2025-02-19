package com.mooncloak.vpn.app.android.play

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.consumePurchase
import com.mooncloak.kodetools.konstruct.annotations.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ConsumePurchaseUseCase @Inject internal constructor() {

    internal suspend operator fun invoke(
        client: BillingClient,
        token: String
    ): Result<Unit> {
        val result: Result<Unit>

        // After we retrieve the tokens and subscription information, we need to consume the purchase, so that plans
        // can be purchased again. If we don't do this, the user can never buy the same plan again.
        withContext(Dispatchers.IO) {
            result = runCatching {
                client.consumePurchase(
                    ConsumeParams.newBuilder()
                        .setPurchaseToken(token)
                        .build()
                )
            }
        }

        return result
    }
}
