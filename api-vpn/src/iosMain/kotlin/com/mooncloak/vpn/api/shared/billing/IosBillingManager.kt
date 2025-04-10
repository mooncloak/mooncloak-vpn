package com.mooncloak.vpn.api.shared.billing

import androidx.compose.ui.text.AnnotatedString
import com.mooncloak.kodetools.textx.AnnotatedText
import com.mooncloak.vpn.api.shared.plan.BillingProvider
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.plan.PlanPeriod
import com.mooncloak.vpn.api.shared.plan.Price
import com.mooncloak.vpn.api.shared.plan.UsageType
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.util.shared.coroutine.PresentationCoroutineScope
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.invoke
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

public abstract class IosBillingManager public constructor(
    private val coroutineScope: PresentationCoroutineScope
) : BillingManager {

    override var isActive: Boolean = false
        protected set

    private val prices = mutableMapOf<String, Price>()

    protected abstract fun startObservingTransactions()
    protected abstract fun stopObservingTransactions()
    protected abstract suspend fun syncExistingTransactions()

    protected abstract suspend fun getProducts(productIds: List<String>): List<Plan>
    protected abstract suspend fun purchaseProduct(plan: Plan): BillingResult

    override fun start() {
        if (!isActive) {
            isActive = true

            startObservingTransactions()

            coroutineScope.launch {
                syncExistingTransactions()
            }
        }
    }

    override fun cancel() {
        if (isActive) {
            isActive = false

            stopObservingTransactions()
        }
    }

    override fun handleResult(token: TransactionToken, productIds: List<String>, state: String?) {

    }

    override suspend fun purchase(plan: Plan): BillingResult {
        if (!isActive) {
            return BillingResult.Failure(code = -1, message = "BillingManager not active")
        }

        prices[plan.id] = plan.price

        return purchaseProduct(plan)
    }

    protected fun createSuccessResult(
        code: Int? = null,
        message: String? = null,
        plans: List<Plan> = emptyList()
    ): BillingResult.Success = BillingResult.Success(
        code = code,
        message = message,
        plans = plans
    )

    protected fun createFailureResult(
        code: Int? = null,
        message: String? = null,
        plans: List<Plan> = emptyList(),
        cause: Throwable? = null
    ): BillingResult.Failure = BillingResult.Failure(
        code = code,
        message = message,
        plans = plans,
        cause = cause
    )

    protected fun createCancelledResult(
        code: Int? = null,
        message: String? = null,
        plans: List<Plan> = emptyList()
    ): BillingResult.Cancelled = BillingResult.Cancelled(
        code = code,
        message = message,
        plans = plans
    )

    protected fun createPlan(
        id: String,
        priceMajor: Double,
        currencyCode: String,
        formattedPrice: String?,
        displayName: String,
        description: String?,
        autoRenews: Boolean,
        subscriptionValue: Int? = null, // e.g., 1 for 1 month
        subscriptionUnit: String? = null, // "day", "week", "month", "year"
        trialValue: Int? = null, // e.g., 7 for 7 days
        trialUnit: String? = null // "day", "week", "month", "year"
    ): Plan {
        val price = createPrice(
            major = priceMajor,
            code = currencyCode,
            formatted = formattedPrice
        )

        val subscriptionPeriod = if (subscriptionValue != null && subscriptionUnit != null) {
            PlanPeriod(
                interval = toDuration(subscriptionValue, subscriptionUnit) ?: 0.days,
                amount = 1 // Single period for StoreKit 2
            )
        } else null

        val trialPeriod = if (trialValue != null && trialUnit != null) {
            PlanPeriod(
                interval = toDuration(trialValue, trialUnit) ?: 0.days,
                amount = 1 // Single trial period
            )
        } else null

        return Plan(
            id = id,
            provider = BillingProvider.Apple,
            price = price,
            conversion = null, // Not available from StoreKit 2
            active = true,    // Default; adjust if server-driven
            live = true,      // Assume live unless sandbox
            autoRenews = autoRenews,
            created = null,   // Not provided by StoreKit 2
            updated = null,
            starts = null,
            ends = null,
            usageType = UsageType.Licensed, // Default; adjust if needed
            trial = trialPeriod,
            subscription = subscriptionPeriod,
            nickname = null,  // Not provided
            title = displayName,
            description = description?.let { AnnotatedText(AnnotatedString(it)) },
            details = null,   // Not provided
            highlight = null,
            self = null,
            taxCode = null,   // Not provided
            metadata = null
        )
    }

    protected fun createTransactionToken(value: String): TransactionToken =
        TransactionToken(value = value)

    private fun createPrice(major: Double, code: String, formatted: String?): Price {
        val currency = Currency(
            type = Currency.Type.Iso4217,
            code = Currency.Code(value = code)
        )

        return Price(
            currency = currency,
            amount = Currency.Amount.invoke(
                currency = currency,
                unit = Currency.Unit.Major,
                value = major
            ).toMinorUnits(),
            formatted = formatted
        )
    }

    // Helper to convert StoreKit 2 unit to Duration
    private fun toDuration(value: Int?, unit: String?): Duration? = when (unit?.lowercase()) {
        "day" -> value?.days
        "week" -> value?.days?.let { it * 7 }
        "month" -> value?.days?.let { it * 30 }
        "year" -> value?.days?.let { it * 365 }
        else -> null
    }

    public interface Factory {

        public fun create(coroutineScope: PresentationCoroutineScope): IosBillingManager

        public companion object
    }
}
