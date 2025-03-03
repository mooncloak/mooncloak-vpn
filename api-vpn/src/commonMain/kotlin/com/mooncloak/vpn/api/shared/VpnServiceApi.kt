package com.mooncloak.vpn.api.shared

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.locale.CountryCode
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.Direction
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.PageRequest.Companion.DEFAULT_COUNT
import com.mooncloak.kodetools.pagex.ResolvedPage
import com.mooncloak.kodetools.pagex.SortOptions
import com.mooncloak.vpn.api.shared.app.Contributor
import com.mooncloak.vpn.api.shared.app.CurrentContributors
import com.mooncloak.vpn.api.shared.billing.BitcoinPlanInvoice
import com.mooncloak.vpn.api.shared.billing.PlanPaymentStatus
import com.mooncloak.vpn.api.shared.billing.ProofOfPurchase
import com.mooncloak.vpn.api.shared.key.Base64Key
import com.mooncloak.vpn.api.shared.location.CountryFilters
import com.mooncloak.vpn.api.shared.plan.AvailablePlans
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.reflection.HttpReflection
import com.mooncloak.vpn.api.shared.server.RegisteredClient
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceSubscriptionUsage
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.api.shared.token.Token
import com.mooncloak.vpn.api.shared.token.TransactionToken
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

@OptIn(ExperimentalApixApi::class)
public interface VpnServiceApi {

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getReflection(
        connectionTimeout: Duration? = null,
        socketTimeout: Duration? = null,
        requestTimeout: Duration? = null
    ): HttpReflection

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getAvailablePlans(): AvailablePlans

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPlan(id: String): Plan

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentInvoice(
        planId: String,
        secret: String? = null,
        token: Token? = null
    ): BitcoinPlanInvoice

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentStatus(
        paymentId: String,
        token: TransactionToken,
        secret: String? = null
    ): PlanPaymentStatus

    /**
     * Exchanges a [TransactionToken] for a set of [ServiceTokens]. This requires the provided [TransactionToken] to be
     * valid, to be associated with the provided [paymentId], for the payment to be successfully processed, for the
     * provided [secret] to be valid for this payment, for the exchange to not have occurred before, and possibly other
     * conditions.
     */
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun exchangeToken(
        receipt: ProofOfPurchase,
        token: Token? = null
    ): ServiceTokens

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun refreshToken(
        refreshToken: Token
    ): ServiceTokens

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun revokeToken(
        refreshToken: Token
    ): Boolean

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCurrentSubscription(
        token: Token
    ): ServiceSubscription

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCurrentSubscriptionUsage(
        token: Token
    ): ServiceSubscriptionUsage

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCountry(
        code: CountryCode
    ): Country

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun paginateCountries(
        direction: Direction = Direction.After,
        cursor: Cursor? = null,
        count: UInt = DEFAULT_COUNT,
        sort: SortOptions? = null,
        filters: CountryFilters? = null
    ): ResolvedPage<Country>

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun registerClient(
        serverId: String,
        clientPublicKey: Base64Key,
        token: Token?
    ): RegisteredClient

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getServer(
        id: String,
        token: Token? = null
    ): Server

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun paginateServers(
        token: Token? = null,
        direction: Direction = Direction.After,
        cursor: Cursor? = null,
        count: UInt = DEFAULT_COUNT,
        sort: SortOptions? = null,
        filters: ServerFilters? = null
    ): ResolvedPage<Server>

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getContributors(): CurrentContributors

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getContributor(id: String): Contributor

    public companion object
}
