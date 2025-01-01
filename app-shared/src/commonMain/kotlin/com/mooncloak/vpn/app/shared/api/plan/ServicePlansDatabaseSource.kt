package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.billing.PaymentProvider
import com.mooncloak.vpn.app.shared.api.money.Currency
import com.mooncloak.vpn.app.shared.api.money.Price
import com.mooncloak.vpn.app.shared.api.money.invoke
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.time.Duration

public class ServicePlansDatabaseSource @Inject public constructor(
    private val database: MooncloakDatabase,
    private val json: Json
) : ServicePlansRepository {

    override suspend fun getPlans(): List<ServicePlan> =
        withContext(Dispatchers.IO) {
            database.servicePlanQueries.selectAll()
                .executeAsList()
                .map { plan -> plan.toVPNServicePlan() }
        }

    override suspend fun getPlan(id: String): ServicePlan =
        withContext(Dispatchers.IO) {
            database.servicePlanQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toVPNServicePlan()
                ?: throw NoSuchElementException("No plan found with id '$id'.")
        }

    internal suspend fun insertAll(plans: List<ServicePlan>) {
        withContext(Dispatchers.IO) {
            database.transaction {
                plans.forEach { plan -> performInsert(plan) }
            }
        }
    }

    internal suspend fun insert(plan: ServicePlan) {
        withContext(Dispatchers.IO) {
            performInsert(plan)
        }
    }

    internal suspend fun delete(id: String) {
        withContext(Dispatchers.IO) {
            database.servicePlanQueries.deleteById(id = id)
        }
    }

    internal suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            database.servicePlanQueries.deleteAll()
        }
    }

    private fun performInsert(plan: ServicePlan) {
        database.servicePlanQueries.insert(
            databaseId = null,
            id = plan.id,
            created = plan.created,
            updated = plan.updated ?: plan.created,
            provider = plan.provider.value,
            active = plan.active,
            usageType = plan.usageType.value,
            live = plan.liveMode,
            nickname = plan.nickname,
            title = plan.title,
            description = plan.description,
            highlight = plan.highlight,
            url = null,
            self = plan.self,
            taxCode = plan.taxCode?.value,
            amount = plan.price.amount,
            amountFormatted = plan.price.formatted,
            currencyType = plan.price.currency.type,
            currencyCode = plan.price.currency.code,
            currencyNumericCode = plan.price.currency.numericCode?.toLong(),
            currencyDefaultFractionDigits = plan.price.currency.defaultFractionDigits?.toLong(),
            currencySymbol = plan.price.currency.symbol,
            duration = plan.details.duration.toIsoString(),
            totalThroughput = plan.details.totalThroughput,
            rxThroughput = plan.details.rxThroughput,
            txThroughput = plan.details.txThroughput,
            trial = plan.trial?.let {
                json.encodeToJsonElement(
                    serializer = TrialPeriod.serializer(),
                    value = it
                )
            },
            subscription = plan.subscription?.let {
                json.encodeToJsonElement(
                    serializer = SubscriptionPeriod.serializer(),
                    value = it
                )
            },
            metadata = plan.metadata,
            breakdown = plan.breakdown?.let {
                json.encodeToJsonElement(
                    serializer = PlanBreakdown.serializer(),
                    value = it
                )
            }
        )
    }

    private fun com.mooncloak.vpn.app.storage.sqlite.database.ServicePlan.toVPNServicePlan(): ServicePlan =
        ServicePlan(
            id = id,
            provider = PaymentProvider(value = this.provider),
            price = Price(
                currency = Currency.invoke(
                    type = currencyType,
                    code = currencyCode,
                    defaultFractionDigits = currencyDefaultFractionDigits?.toInt(),
                    numericCode = currencyNumericCode?.toInt(),
                    symbol = currencySymbol
                ),
                amount = amount,
                formatted = amountFormatted
            ),
            cryptoEstimate = null,
            active = active,
            created = created,
            updated = updated,
            usageType = UsageType(value = usageType),
            trial = trial?.let {
                json.decodeFromJsonElement(
                    deserializer = TrialPeriod.serializer(),
                    element = it
                )
            },
            subscription = subscription?.let {
                json.decodeFromJsonElement(
                    deserializer = SubscriptionPeriod.serializer(),
                    element = it
                )
            },
            liveMode = live,
            nickname = nickname,
            title = title,
            description = description,
            highlight = highlight,
            self = self,
            metadata = metadata,
            taxCode = taxCode?.let { TaxCode(value = it) },
            breakdown = breakdown?.let {
                json.decodeFromJsonElement(
                    deserializer = PlanBreakdown.serializer(),
                    element = it
                )
            },
            details = ServicePlanDetails(
                duration = Duration.parseIsoString(duration),
                totalThroughput = totalThroughput,
                rxThroughput = rxThroughput,
                txThroughput = txThroughput
            )
        )
}
