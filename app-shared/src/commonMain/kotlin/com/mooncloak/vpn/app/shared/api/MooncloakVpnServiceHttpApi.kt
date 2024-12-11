package com.mooncloak.vpn.app.shared.api

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.Direction
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.Page
import com.mooncloak.kodetools.pagex.PageRequest
import com.mooncloak.kodetools.pagex.PageRequest.Companion.DEFAULT_COUNT
import com.mooncloak.kodetools.pagex.SortOptions
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalApixApi::class)
public class MooncloakVpnServiceHttpApi @Inject public constructor(
    private val httpClient: HttpClient
) {

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getAvailablePlans(): AvailablePlans {
        val response = httpClient.get("https://mooncloak.com/api/vpn/plans")

        return response.body<HttpResponseBody<AvailablePlans>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentInfo(
        planId: String,
        secret: String? = null
    ): PlanInvoice {
        val response = httpClient.post("https://mooncloak.com/api/vpn/payment/invoice") {
            setBody(
                GetPaymentInfoRequestBody(
                    planId = planId,
                    secret = secret
                )
            )
        }

        return response.body<HttpResponseBody<PlanInvoice>>().getOrThrow()
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
        paymentId: String,
        token: TransactionToken,
        secret: String? = null
    ): ServiceTokens {
        val response = httpClient.post("https://mooncloak.com/api/vpn/token/exchange") {
            bearerAuth(token.value)

            setBody(
                ExchangeTokenRequestBody(
                    paymentId = paymentId,
                    secret = secret
                )
            )
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
    public suspend fun registerWireGuardPublicKey() {
        val response = httpClient.post("https://mooncloak.com/api/vpn/wireguard/register")

        TODO()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCountry(
        code: CountryCode
    ): Country {
        val response = httpClient.get("https://mooncloak.com/api/vpn/country/${code.value}")

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
    ): Page<Country> {
        val pageRequest = PageRequest<String, CountryFilters>(
            data = null,
            direction = direction,
            cursor = cursor,
            count = count,
            sort = sort,
            filters = filters
        )

        val response = httpClient.post("https://mooncloak.com/api/vpn/country") {
            setBody(pageRequest)
        }

        return response.body<HttpResponseBody<Page<Country>>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getRegion(
        code: RegionCode
    ): Region {
        val response = httpClient.get("https://mooncloak.com/api/vpn/region/${code.value}")

        return response.body<HttpResponseBody<Region>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getServer(
        id: String,
        token: Token? = null
    ): Server {
        val response = httpClient.get("https://mooncloak.com/api/vpn/server/$id") {
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
    ): Page<Server> {
        val pageRequest = PageRequest<String, ServerFilters>(
            data = null,
            direction = direction,
            cursor = cursor,
            count = count,
            sort = sort,
            filters = filters
        )

        val response = httpClient.post("https://mooncloak.com/api/vpn/server") {
            token?.value?.let { bearerAuth(it) }

            setBody(pageRequest)
        }

        return response.body<HttpResponseBody<Page<Server>>>().getOrThrow()
    }
}
