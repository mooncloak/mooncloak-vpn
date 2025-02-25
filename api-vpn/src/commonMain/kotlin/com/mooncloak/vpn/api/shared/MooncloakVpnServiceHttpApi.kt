package com.mooncloak.vpn.api.shared

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.kodetools.locale.Country
import com.mooncloak.kodetools.locale.CountryCode
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.Direction
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.PageRequest
import com.mooncloak.kodetools.pagex.PageRequest.Companion.DEFAULT_COUNT
import com.mooncloak.kodetools.pagex.ResolvedPage
import com.mooncloak.kodetools.pagex.SortOptions
import com.mooncloak.vpn.api.shared.app.Contributor
import com.mooncloak.vpn.api.shared.app.CurrentContributors
import com.mooncloak.vpn.api.shared.billing.BitcoinPlanInvoice
import com.mooncloak.vpn.api.shared.billing.GetPaymentInvoiceRequestBody
import com.mooncloak.vpn.api.shared.billing.GetPaymentStatusRequestBody
import com.mooncloak.vpn.api.shared.billing.PlanPaymentStatus
import com.mooncloak.vpn.api.shared.billing.ProofOfPurchase
import com.mooncloak.vpn.api.shared.key.Base64Key
import com.mooncloak.vpn.api.shared.location.CountryFilters
import com.mooncloak.vpn.api.shared.plan.AvailablePlans
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.reflection.HttpReflection
import com.mooncloak.vpn.api.shared.server.ClientRegistrationRequestBody
import com.mooncloak.vpn.api.shared.server.RegisteredClient
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceSubscriptionUsage
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.api.shared.token.RevokeTokenRequestBody
import com.mooncloak.vpn.api.shared.token.RevokedTokenResponseBody
import com.mooncloak.vpn.api.shared.token.Token
import com.mooncloak.vpn.api.shared.token.TokenTypeHint
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalApixApi::class)
public class MooncloakVpnServiceHttpApi public constructor(
    private val httpClient: HttpClient
) {

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getReflection(): HttpReflection = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get("https://mooncloak.com/api/mirror")

        return@withContext response.body<HttpResponseBody<HttpReflection>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getAvailablePlans(): AvailablePlans = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get("https://mooncloak.com/api/billing/plans")

        return@withContext response.body<HttpResponseBody<AvailablePlans>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPlan(id: String): Plan = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get("https://mooncloak.com/api/billing/plan/$id")

        return@withContext response.body<HttpResponseBody<Plan>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentInvoice(
        planId: String,
        secret: String? = null,
        token: Token? = null
    ): BitcoinPlanInvoice = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/payment/invoice") {
            token?.value?.let { bearerAuth(it) }

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(
                GetPaymentInvoiceRequestBody(
                    planId = planId,
                    secret = secret
                )
            )
        }

        return@withContext response.body<HttpResponseBody<BitcoinPlanInvoice>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentStatus(
        paymentId: String,
        token: TransactionToken,
        secret: String? = null
    ): PlanPaymentStatus = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/payment/status") {
            bearerAuth(token.value)

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(
                GetPaymentStatusRequestBody(
                    paymentId = paymentId,
                    secret = secret
                )
            )
        }

        return@withContext response.body<HttpResponseBody<PlanPaymentStatus>>().getOrThrow()
    }

    /**
     * Exchanges a [TransactionToken] for a set of [ServiceTokens]. This requires the provided [TransactionToken] to be
     * valid, to be associated with the provided [paymentId], for the payment to be successfully processed, for the
     * provided [secret] to be valid for this payment, for the exchange to not have occurred before, and possibly other
     * conditions.
     */
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun exchangeToken(
        receipt: ProofOfPurchase
    ): ServiceTokens = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/token/exchange") {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(receipt)
        }

        return@withContext response.body<HttpResponseBody<ServiceTokens>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun refreshToken(
        refreshToken: Token
    ): ServiceTokens = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/token/refresh") {
            bearerAuth(refreshToken.value)
        }

        return@withContext response.body<HttpResponseBody<ServiceTokens>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun revokeToken(
        refreshToken: Token
    ): Boolean = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/token/revoke") {
            bearerAuth(refreshToken.value)

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(
                RevokeTokenRequestBody(
                    token = refreshToken,
                    hint = TokenTypeHint.RefreshToken
                )
            )
        }

        return@withContext response.body<HttpResponseBody<RevokedTokenResponseBody>>().getOrThrow().success
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCurrentSubscription(
        token: Token
    ): ServiceSubscription = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/subscription") {
            bearerAuth(token.value)
        }

        return@withContext response.body<HttpResponseBody<ServiceSubscription>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCurrentSubscriptionUsage(
        token: Token
    ): ServiceSubscriptionUsage = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/subscription/usage") {
            bearerAuth(token.value)
        }

        return@withContext response.body<HttpResponseBody<ServiceSubscriptionUsage>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getCountry(
        code: CountryCode
    ): Country = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get("https://mooncloak.com/api/vpn/service/country/${code.value}")

        return@withContext response.body<HttpResponseBody<Country>>().getOrThrow()
    }

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    public suspend fun paginateCountries(
        direction: Direction = Direction.After,
        cursor: Cursor? = null,
        count: UInt = DEFAULT_COUNT,
        sort: SortOptions? = null,
        filters: CountryFilters? = null
    ): ResolvedPage<Country> = withContext(Dispatchers.PlatformIO) {
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

        return@withContext response.body<HttpResponseBody<ResolvedPage<Country>>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun registerClient(
        serverId: String,
        clientPublicKey: Base64Key,
        token: Token?
    ): RegisteredClient = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post("https://mooncloak.com/api/vpn/service/client/register") {
            token?.value?.let { bearerAuth(it) }

            contentType(ContentType.Application.Json)

            setBody(
                ClientRegistrationRequestBody(
                    serverId = serverId,
                    publicKey = clientPublicKey
                )
            )
        }

        return@withContext response.body<HttpResponseBody<RegisteredClient>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getServer(
        id: String,
        token: Token? = null
    ): Server = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get("https://mooncloak.com/api/vpn/service/server/$id") {
            token?.value?.let { bearerAuth(it) }
        }

        return@withContext response.body<HttpResponseBody<Server>>().getOrThrow()
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
    ): ResolvedPage<Server> = withContext(Dispatchers.PlatformIO) {
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

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(pageRequest)
        }

        return@withContext response.body<HttpResponseBody<ResolvedPage<Server>>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getContributors(): CurrentContributors = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get("https://mooncloak.com/api/vpn/app/contributor")

        return@withContext response.body<HttpResponseBody<CurrentContributors>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getContributor(id: String): Contributor = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get("https://mooncloak.com/api/vpn/app/contributor/$id")

        return@withContext response.body<HttpResponseBody<Contributor>>().getOrThrow()
    }
}
