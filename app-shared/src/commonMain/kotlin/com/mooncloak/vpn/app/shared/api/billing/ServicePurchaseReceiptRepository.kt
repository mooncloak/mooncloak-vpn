package com.mooncloak.vpn.app.shared.api.billing

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
        planId: String,
        invoiceId: String? = null,
        purchased: Instant,
        provider: PaymentProvider,
        created: Instant? = null,
        updated: Instant? = null,
        subscription: Boolean = false,
        clientSecret: String? = null,
        token: TransactionToken,
        signature: String? = null,
        quantity: Int? = null
    ): ServicePurchaseReceipt

    public suspend fun remove(id: String)

    public companion object
}
