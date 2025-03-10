package com.mooncloak.vpn.api.shared.billing

import com.mooncloak.vpn.api.shared.plan.BillingProvider
import com.mooncloak.vpn.api.shared.plan.Price
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.data.shared.repository.MutableRepository
import com.mooncloak.vpn.data.shared.repository.Repository
import kotlinx.datetime.Instant
import kotlin.coroutines.cancellation.CancellationException

public interface ServicePurchaseReceiptRepository : Repository<ServicePurchaseReceipt> {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun getByOrderId(orderId: String): ServicePurchaseReceipt

    public suspend fun getLatest(): ServicePurchaseReceipt?

    public companion object
}

public interface MutableServicePurchaseReceiptRepository : ServicePurchaseReceiptRepository,
    MutableRepository<ServicePurchaseReceipt> {

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

    public companion object
}

public suspend fun ServicePurchaseReceiptRepository.getByOrderIdOrNull(orderId: String): ServicePurchaseReceipt? =
    try {
        getByOrderId(orderId = orderId)
    } catch (_: NoSuchElementException) {
        null
    }
