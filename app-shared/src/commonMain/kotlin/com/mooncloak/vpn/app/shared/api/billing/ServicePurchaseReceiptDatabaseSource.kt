package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.plan.BillingProvider
import com.mooncloak.vpn.app.shared.api.plan.Price
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import com.mooncloak.vpn.app.shared.storage.database.MooncloakDatabaseProvider
import com.mooncloak.vpn.app.storage.sqlite.database.PurchaseReceipt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class ServicePurchaseReceiptDatabaseSource @Inject public constructor(
    private val databaseProvider: MooncloakDatabaseProvider,
    private val clock: Clock,
    private val json: Json
) : ServicePurchaseReceiptRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun get(id: String): ServicePurchaseReceipt =
        withContext(Dispatchers.IO) {
            val database = databaseProvider.get()

            return@withContext database.purchaseReceiptQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toServicePurchaseReceipt()
                ?: throw NoSuchElementException("No ServicePurchaseReceipt found with id '$id'.")
        }

    override suspend fun getPage(count: Int, offset: Int): List<ServicePurchaseReceipt> =
        withContext(Dispatchers.IO) {
            val database = databaseProvider.get()

            return@withContext database.purchaseReceiptQueries.selectPage(
                count = count.toLong(),
                offset = offset.toLong()
            )
                .executeAsList()
                .map { receipt -> receipt.toServicePurchaseReceipt() }
        }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun add(
        orderId: String?,
        planIds: List<String>,
        invoiceId: String?,
        purchased: Instant,
        provider: BillingProvider,
        created: Instant?,
        updated: Instant?,
        subscription: Boolean,
        clientSecret: String?,
        token: TransactionToken,
        signature: String?,
        quantity: Int?,
        price: Price?
    ): ServicePurchaseReceipt =
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val database = databaseProvider.get()

                val now = clock.now()
                val id = Uuid.random().toHexString()

                database.transactionWithResult {
                    database.purchaseReceiptQueries.insert(
                        database_id = null,
                        id = id,
                        order_id = orderId,
                        plan_id = planIds.firstOrNull(),
                        plan_ids = json.encodeToJsonElement(
                            serializer = ListSerializer(String.serializer()),
                            value = planIds
                        ),
                        invoice_id = invoiceId,
                        created = created ?: now,
                        updated = updated ?: now,
                        purchased = purchased,
                        provider = provider.value,
                        subscription = subscription,
                        client_secret = clientSecret,
                        token = token.value,
                        signature = signature,
                        quantity = quantity?.toLong(),
                        price = price?.let {
                            json.encodeToJsonElement(
                                serializer = Price.serializer(),
                                value = it
                            )
                        }
                    )

                    return@transactionWithResult database.purchaseReceiptQueries.selectById(id = id)
                        .executeAsOne()
                        .toServicePurchaseReceipt()
                }
            }
        }

    override suspend fun remove(id: String) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val database = databaseProvider.get()

                return@withContext database.purchaseReceiptQueries.deleteById(id = id)
            }
        }
    }

    private fun PurchaseReceipt.toServicePurchaseReceipt(): ServicePurchaseReceipt =
        ServicePurchaseReceipt(
            id = this.id,
            orderId = this.order_id,
            planIds = runCatching {
                json.decodeFromJsonElement(
                    deserializer = ListSerializer(String.serializer()),
                    element = this.plan_ids
                )
            }.getOrNull()
                ?: listOfNotNull(this.plan_id),
            invoiceId = this.invoice_id,
            created = this.created,
            updated = this.updated,
            purchased = this.purchased,
            provider = BillingProvider(value = this.provider),
            subscription = this.subscription,
            clientSecret = this.client_secret,
            token = TransactionToken(value = this.token),
            signature = this.signature,
            quantity = this.quantity?.toInt(),
            price = this.price?.let { element ->
                json.decodeFromJsonElement(
                    deserializer = Price.serializer(),
                    element = element
                )
            }
        )
}
