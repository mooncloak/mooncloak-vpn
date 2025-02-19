package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.vpn.app.shared.api.plan.BillingProvider
import com.mooncloak.vpn.app.shared.api.plan.Price
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import kotlinx.coroutines.CancellationException
import kotlinx.datetime.Instant

public interface ServicePurchaseReceiptRepository {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): ServicePurchaseReceipt

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getByOrderId(orderId: String): ServicePurchaseReceipt

    public suspend fun getLatest(): ServicePurchaseReceipt?

    public suspend fun getPage(
        count: Int = 20,
        offset: Int = 0
    ): List<ServicePurchaseReceipt>

    public companion object
}

public interface MutableServicePurchaseReceiptRepository : ServicePurchaseReceiptRepository {

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

    public suspend fun clear()

    public companion object
}

public suspend fun ServicePurchaseReceiptRepository.getOrNull(id: String): ServicePurchaseReceipt? =
    try {
        get(id = id)
    } catch (_: NoSuchElementException) {
        null
    }

public suspend fun ServicePurchaseReceiptRepository.getByOrderIdOrNull(orderId: String): ServicePurchaseReceipt? =
    try {
        getByOrderId(orderId = orderId)
    } catch (_: NoSuchElementException) {
        null
    }
