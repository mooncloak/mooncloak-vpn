package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.app.storage.sqlite.database.PurchaseReceipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class ServicePurchaseReceiptDatabaseSource @Inject public constructor(
    private val database: MooncloakDatabase,
    private val clock: Clock
) : ServicePurchaseReceiptRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): ServicePurchaseReceipt =
        withContext(Dispatchers.IO) {
            database.purchaseReceiptQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toServicePurchaseReceipt()
                ?: throw NoSuchElementException("No ServicePurchaseReceipt found with id '$id'.")
        }

    override suspend fun getPage(count: Int, offset: Int): List<ServicePurchaseReceipt> =
        withContext(Dispatchers.IO) {
            database.purchaseReceiptQueries.selectPage(count = count.toLong(), offset = offset.toLong())
                .executeAsList()
                .map { receipt -> receipt.toServicePurchaseReceipt() }
        }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun add(
        planId: String,
        invoiceId: String?,
        purchased: Instant,
        provider: PaymentProvider,
        created: Instant?,
        updated: Instant?,
        subscription: Boolean,
        clientSecret: String?,
        token: TransactionToken,
        signature: String?,
        quantity: Int?
    ): ServicePurchaseReceipt =
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val now = clock.now()
                val id = Uuid.random().toHexString()

                database.transactionWithResult {
                    database.purchaseReceiptQueries.insert(
                        databaseId = null,
                        id = id,
                        planId = planId,
                        invoiceId = invoiceId,
                        created = created ?: now,
                        updated = updated ?: now,
                        purchased = purchased,
                        provider = provider.value,
                        subscription = subscription,
                        clientSecret = clientSecret,
                        token = token.value,
                        signature = signature,
                        quantity = quantity?.toLong()
                    )

                    database.purchaseReceiptQueries.selectById(id = id)
                        .executeAsOne()
                        .toServicePurchaseReceipt()
                }
            }
        }

    override suspend fun remove(id: String) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                database.purchaseReceiptQueries.deleteById(id = id)
            }
        }
    }

    private fun PurchaseReceipt.toServicePurchaseReceipt(): ServicePurchaseReceipt =
        ServicePurchaseReceipt(
            id = this.id,
            planId = this.planId,
            invoiceId = this.invoiceId,
            created = this.created,
            updated = this.updated,
            purchased = this.purchased,
            provider = PaymentProvider(value = this.provider),
            subscription = this.subscription,
            clientSecret = this.clientSecret,
            token = TransactionToken(value = this.token),
            signature = this.signature,
            quantity = this.quantity?.toInt()
        )
}
