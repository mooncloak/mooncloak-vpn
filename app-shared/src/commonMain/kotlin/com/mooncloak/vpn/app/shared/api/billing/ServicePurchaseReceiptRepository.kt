package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.vpn.app.shared.api.plan.BillingProvider
import com.mooncloak.vpn.app.shared.api.plan.Price
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import kotlinx.coroutines.CancellationException
import kotlinx.datetime.Instant

public interface ServicePurchaseReceiptRepository {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): ServicePurchaseReceipt

    public suspend fun getPage(
        count: Int = 20,
        offset: Int = 0
    ): List<ServicePurchaseReceipt>

    public suspend fun add(
        orderId: String? = null,
        planIds: List<String>,
        invoiceId: String? = null,
        purchased: Instant,
        provider: BillingProvider,
        created: Instant? = null,
        updated: Instant? = null,
        subscription: Boolean = false,
        clientSecret: String? = null,
        token: TransactionToken,
        signature: String? = null,
        quantity: Int? = null,
        price: Price? = null
    ): ServicePurchaseReceipt

    public suspend fun remove(id: String)

    public companion object
}
