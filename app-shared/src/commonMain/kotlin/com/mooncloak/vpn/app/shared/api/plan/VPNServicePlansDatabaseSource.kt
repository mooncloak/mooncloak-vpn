package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.money.Currency
import com.mooncloak.vpn.app.shared.api.money.Price
import com.mooncloak.vpn.app.shared.api.money.invoke
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.app.storage.sqlite.database.ServicePlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.time.Duration

public class VPNServicePlansDatabaseSource @Inject public constructor(
    private val database: MooncloakDatabase,
    private val json: Json
) : VPNServicePlansRepository {

    override suspend fun getPlans(): List<VPNServicePlan> =
        withContext(Dispatchers.IO) {
            database.servicePlanQueries.selectAll()
                .executeAsList()
                .map { plan -> plan.toVPNServicePlan() }
        }

    override suspend fun getPlan(id: String): VPNServicePlan =
        withContext(Dispatchers.IO) {
            database.servicePlanQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toVPNServicePlan()
                ?: throw NoSuchElementException("No plan found with id '$id'.")
        }

    internal suspend fun insertAll(plans: List<VPNServicePlan>) {
        withContext(Dispatchers.IO) {
            database.transaction {
                plans.forEach { plan -> performInsert(plan) }
            }
        }
    }

    internal suspend fun insert(plan: VPNServicePlan) {
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

    private fun performInsert(plan: VPNServicePlan) {
        database.servicePlanQueries.insert(
            databaseId = null,
            id = plan.id,
            created = plan.created,
            updated = plan.updated ?: plan.created,
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
            duration = plan.duration.toIsoString(),
            totalThroughput = plan.totalThroughput,
            rxThroughput = plan.rxThroughput,
            txThroughput = plan.txThroughput,
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

    private fun ServicePlan.toVPNServicePlan(): VPNServicePlan = VPNServicePlan(
        id = this.id,
        price = Price(
            currency = Currency.invoke(
                type = this.currencyType,
                code = this.currencyCode,
                defaultFractionDigits = this.currencyDefaultFractionDigits?.toInt(),
                numericCode = this.currencyNumericCode?.toInt(),
                symbol = this.currencySymbol
            ),
            amount = this.amount,
            formatted = this.amountFormatted
        ),
        cryptoEstimate = null,
        active = this.active,
        created = this.created,
        updated = this.updated,
        usageType = UsageType(value = this.usageType),
        trial = this.trial?.let {
            json.decodeFromJsonElement(
                deserializer = TrialPeriod.serializer(),
                element = it
            )
        },
        subscription = this.subscription?.let {
            json.decodeFromJsonElement(
                deserializer = SubscriptionPeriod.serializer(),
                element = it
            )
        },
        liveMode = this.live,
        nickname = this.nickname,
        title = this.title,
        description = this.description,
        highlight = this.highlight,
        self = this.self,
        metadata = this.metadata,
        taxCode = this.taxCode?.let { TaxCode(value = it) },
        breakdown = this.breakdown?.let {
            json.decodeFromJsonElement(
                deserializer = PlanBreakdown.serializer(),
                element = it
            )
        },
        duration = Duration.parseIsoString(this.duration),
        totalThroughput = this.totalThroughput,
        rxThroughput = this.rxThroughput,
        txThroughput = this.txThroughput
    )
}
