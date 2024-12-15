package com.mooncloak.vpn.app.shared.api

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.Direction
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.PageRequest
import com.mooncloak.kodetools.pagex.PageRequest.Companion.DEFAULT_COUNT
import com.mooncloak.kodetools.pagex.ResolvedPage
import com.mooncloak.kodetools.pagex.SortOptions
import com.mooncloak.vpn.app.shared.api.billing.BitcoinPlanInvoice
import com.mooncloak.vpn.app.shared.api.billing.GetPaymentInvoiceRequestBody
import com.mooncloak.vpn.app.shared.api.billing.GetPaymentStatusRequestBody
import com.mooncloak.vpn.app.shared.api.billing.PlanPaymentStatus
import com.mooncloak.vpn.app.shared.api.billing.PurchaseReceipt
import com.mooncloak.vpn.app.shared.api.location.Country
import com.mooncloak.vpn.app.shared.api.location.CountryCode
import com.mooncloak.vpn.app.shared.api.location.CountryFilters
import com.mooncloak.vpn.app.shared.api.plan.AvailablePlans
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.ServerFilters
import com.mooncloak.vpn.app.shared.api.service.ServiceSubscription
import com.mooncloak.vpn.app.shared.api.service.ServiceTokens
import com.mooncloak.vpn.app.shared.api.token.RevokeTokenRequestBody
import com.mooncloak.vpn.app.shared.api.token.RevokedTokenResponseBody
import com.mooncloak.vpn.app.shared.api.token.Token
import com.mooncloak.vpn.app.shared.api.token.TokenTypeHint
import com.mooncloak.vpn.app.shared.api.token.TransactionToken
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalApixApi::class)
public class MooncloakVpnServiceHttpApi @Inject public constructor(
    private val httpClient: HttpClient
) {

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getAvailablePlans(): AvailablePlans {
        val response = httpClient.get("https://mooncloak.com/api/vpn/marketing/plans")

        return response.body<HttpResponseBody<AvailablePlans>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentInvoice(
        planId: String,
        secret: String? = null,
        token: Token? = null
    ): BitcoinPlanInvoice {
        val response = httpClient.post("https://mooncloak.com/api/vpn/payment/invoice") {
            token?.value?.let { bearerAuth(it) }

            setBody(
                GetPaymentInvoiceRequestBody(
                    planId = planId,
                    secret = secret
                )
            )
        }

        return response.body<HttpResponseBody<BitcoinPlanInvoice>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentStatus(
        paymentId: String,
        token: TransactionToken,
        secret: String? = null
    ): PlanPaymentStatus {
        val response = httpClient.post("https://mooncloak.com/api/vpn/payment/status") {
            bearerAuth(token.value)

            setBody(
                GetPaymentStatusRequestBody(
                    paymentId = paymentId,
                    secret = secret
                )
            )
        }

        return response.body<HttpResponseBody<PlanPaymentStatus>>().getOrThrow()
    }

    /**
     * Exchanges a [TransactionToken] for a [ServiceTokens]. This requires the provided [TransactionToken] to be valid,
     * to be associated with the provided [paymentId], for the payment to be successfully processed, for the provided
     * [secret] to be valid for this payment, for the exchange to not have occurred before, and possibly other
     * conditions.
     */
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun exchangeToken(
        receipt: PurchaseReceipt
    ): ServiceTokens {
        val response = httpClient.post("https://mooncloak.com/api/vpn/token/exchange") {
            setBody(receipt)
        }

        return response.body<HttpResponseBody<ServiceTokens>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun refreshToken(
        refreshToken: Token
    ): ServiceTokens {
        val response = httpClient.post("https://mooncloak.com/api/vpn/token/refresh") {
            bearerAuth(refreshToken.value)
        }

        return response.body<HttpResponseBody<ServiceTokens>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun revokeToken(
        refreshToken: Token
    ): Boolean {
        val response = httpClient.post("https://mooncloak.com/api/vpn/token/revoke") {
            bearerAuth(refreshToken.value)

            setBody(
                RevokeTokenRequestBody(
                    token = refreshToken,
                    hint = TokenTypeHint.RefreshToken
                )
            )
        }

        return response.body<HttpResponseBody<RevokedTokenResponseBody>>().getOrThrow().success
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCurrentSubscription(
        token: Token
    ): ServiceSubscription {
        val response = httpClient.post("https://mooncloak.com/api/vpn/subscription") {
            bearerAuth(token.value)
        }

        return response.body<HttpResponseBody<ServiceSubscription>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun registerWireGuardPublicKey() {
        val response = httpClient.post("https://mooncloak.com/api/vpn/wireguard/register")

        TODO()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCountry(
        code: CountryCode
    ): Country {
        val response = httpClient.get("https://mooncloak.com/api/vpn/service/country/${code.value}")

        return response.body<HttpResponseBody<Country>>().getOrThrow()
    }

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun paginateCountries(
        direction: Direction = Direction.After,
        cursor: Cursor? = null,
        count: UInt = DEFAULT_COUNT,
        sort: SortOptions? = null,
        filters: CountryFilters? = null
    ): ResolvedPage<Country> {
        val pageRequest = PageRequest<String, CountryFilters>(
            data = null,
            direction = direction,
            cursor = cursor,
            count = count,
            sort = sort,
            filters = filters
        )

        val response = httpClient.post("https://mooncloak.com/api/vpn/service/country") {
            contentType(ContentType.Application.Json)

            setBody(pageRequest)
        }

        return response.body<HttpResponseBody<ResolvedPage<Country>>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getServer(
        id: String,
        token: Token? = null
    ): Server {
        val response = httpClient.get("https://mooncloak.com/api/vpn/service/server/$id") {
            token?.value?.let { bearerAuth(it) }
        }

        return response.body<HttpResponseBody<Server>>().getOrThrow()
    }

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun paginateServers(
        token: Token? = null,
        direction: Direction = Direction.After,
        cursor: Cursor? = null,
        count: UInt = DEFAULT_COUNT,
        sort: SortOptions? = null,
        filters: ServerFilters? = null
    ): ResolvedPage<Server> {
        val pageRequest = PageRequest<String, ServerFilters>(
            data = null,
            direction = direction,
            cursor = cursor,
            count = count,
            sort = sort,
            filters = filters
        )

        val response = httpClient.post("https://mooncloak.com/api/vpn/service/server") {
            token?.value?.let { bearerAuth(it) }

            setBody(pageRequest)
        }

        return response.body<HttpResponseBody<ResolvedPage<Server>>>().getOrThrow()
    }
}
