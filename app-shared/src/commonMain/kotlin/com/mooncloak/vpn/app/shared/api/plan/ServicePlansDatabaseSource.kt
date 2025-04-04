package com.mooncloak.vpn.app.shared.api.plan

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.textx.PlainText
import com.mooncloak.kodetools.textx.TextContent
import com.mooncloak.vpn.api.shared.plan.BillingProvider
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.plan.PlanPeriod
import com.mooncloak.vpn.api.shared.plan.Price
import com.mooncloak.vpn.api.shared.plan.ServicePlansRepository
import com.mooncloak.vpn.api.shared.plan.TaxCode
import com.mooncloak.vpn.api.shared.plan.UsageType
import com.mooncloak.vpn.api.shared.plan.duration
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull

public class ServicePlansDatabaseSource @Inject public constructor(
    private val database: MooncloakDatabase,
    private val json: Json,
    private val clock: Clock
) : ServicePlansRepository {

    override suspend fun getPlans(): List<Plan> =
        withContext(Dispatchers.PlatformIO) {
            database.servicePlanQueries.selectAll()
                .executeAsList()
                .map { plan -> plan.toVPNServicePlan() }
        }

    override suspend fun getPlan(id: String): Plan =
        withContext(Dispatchers.PlatformIO) {
            database.servicePlanQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toVPNServicePlan()
                ?: throw NoSuchElementException("No plan found with id '$id'.")
        }

    internal suspend fun insertAll(plans: List<Plan>) {
        withContext(Dispatchers.PlatformIO) {
            database.transaction {
                plans.forEach { plan -> performInsert(plan) }
            }
        }
    }

    internal suspend fun insert(plan: Plan) {
        withContext(Dispatchers.PlatformIO) {
            performInsert(plan)
        }
    }

    internal suspend fun delete(id: String) {
        withContext(Dispatchers.PlatformIO) {
            database.servicePlanQueries.deleteById(id = id)
        }
    }

    internal suspend fun deleteAll() {
        withContext(Dispatchers.PlatformIO) {
            database.servicePlanQueries.deleteAll()
        }
    }

    private fun performInsert(plan: Plan) {
        val now = clock.now()

        database.servicePlanQueries.insert(
            databaseId = null,
            id = plan.id,
            created = plan.created ?: now,
            updated = plan.updated ?: plan.created ?: now,
            provider = plan.provider.value,
            active = plan.active,
            usageType = plan.usageType.value,
            live = plan.live,
            nickname = plan.nickname,
            title = plan.title,
            description = plan.description?.let {
                json.encodeToJsonElement(
                    serializer = TextContent.serializer(),
                    value = it
                )
            },
            details = plan.details?.let {
                json.encodeToJsonElement(
                    serializer = TextContent.serializer(),
                    value = it
                )
            },
            highlight = plan.highlight,
            url = null,
            self = plan.self,
            taxCode = plan.taxCode?.value,
            amount = plan.price.amount,
            amountFormatted = plan.price.formatted,
            currencyType = plan.price.currency.type.value,
            currencyCode = plan.price.currency.code.value,
            currencyNumericCode = plan.price.currency.numericCode?.toLong(),
            currencyDefaultFractionDigits = plan.price.currency.defaultFractionDigits?.toLong(),
            currencySymbol = plan.price.currency.symbol,
            currencyAddress = plan.price.currency.address,
            duration = plan.subscription?.duration?.toIsoString() ?: "",
            trial = plan.trial?.let {
                json.encodeToJsonElement(
                    serializer = PlanPeriod.serializer(),
                    value = it
                )
            },
            subscription = plan.subscription?.let {
                json.encodeToJsonElement(
                    serializer = PlanPeriod.serializer(),
                    value = it
                )
            },
            metadata = plan.metadata
        )
    }

    private fun com.mooncloak.vpn.data.sqlite.database.ServicePlan.toVPNServicePlan(): Plan =
        Plan(
            id = id,
            provider = BillingProvider(value = this.provider),
            price = Price(
                currency = Currency(
                    type = Currency.Type(value = currencyType),
                    code = Currency.Code(value = currencyCode),
                    defaultFractionDigits = currencyDefaultFractionDigits?.toInt(),
                    numericCode = currencyNumericCode?.toInt(),
                    symbol = currencySymbol,
                    address = currencyAddress
                ),
                amount = amount,
                formatted = amountFormatted
            ),
            conversion = null,
            active = active,
            created = created,
            updated = updated,
            usageType = UsageType(value = usageType),
            trial = trial?.let {
                json.decodeFromJsonElement(
                    deserializer = PlanPeriod.serializer(),
                    element = it
                )
            },
            subscription = subscription?.let {
                json.decodeFromJsonElement(
                    deserializer = PlanPeriod.serializer(),
                    element = it
                )
            },
            live = live,
            nickname = nickname,
            title = title,
            description = description?.toTextContentOrNull(),
            details = details?.toTextContentOrNull(),
            highlight = highlight,
            self = self,
            metadata = metadata,
            taxCode = taxCode?.let { TaxCode(value = it) }
        )

    private fun JsonElement.toTextContentOrNull(): TextContent? =
        try {
            when (this) {
                JsonNull -> null
                is JsonPrimitive -> this.contentOrNull?.let { PlainText(value = it) }
                else -> json.decodeFromJsonElement(
                    deserializer = TextContent.serializer(),
                    element = this
                )
            }
        } catch (e: Exception) {
            LogPile.error(
                message = "Error converting 'JsonElement' to 'AnnotatedString'.",
                cause = e
            )

            // For some reason the TextContent isn't serializing/deserializing correctly. Try and explicitly handle the
            // conversion to plain text content.
            val content = (this as? JsonObject)
                ?.get("value")
                ?.let { it as? JsonPrimitive }
                ?.contentOrNull
                ?.let { PlainText(value = it) }

            content
        }
}
