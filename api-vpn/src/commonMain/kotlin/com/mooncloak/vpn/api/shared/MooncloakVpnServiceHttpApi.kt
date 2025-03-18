package com.mooncloak.vpn.api.shared

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.kodetools.locale.CountryCode
import com.mooncloak.kodetools.pagex.Cursor
import com.mooncloak.kodetools.pagex.Direction
import com.mooncloak.kodetools.pagex.ExperimentalPaginationAPI
import com.mooncloak.kodetools.pagex.PageRequest
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
import com.mooncloak.vpn.api.shared.location.CountryDetails
import com.mooncloak.vpn.api.shared.location.CountryFilters
import com.mooncloak.vpn.api.shared.location.CountryPage
import com.mooncloak.vpn.api.shared.plan.AvailablePlans
import com.mooncloak.vpn.api.shared.plan.Plan
import com.mooncloak.vpn.api.shared.provider.HostUrlProvider
import com.mooncloak.vpn.api.shared.reflection.HttpReflection
import com.mooncloak.vpn.api.shared.server.ClientRegistrationRequestBody
import com.mooncloak.vpn.api.shared.server.RegisteredClient
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerFilters
import com.mooncloak.vpn.api.shared.service.ServiceSubscription
import com.mooncloak.vpn.api.shared.service.ServiceSubscriptionUsage
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.api.shared.support.FAQPage
import com.mooncloak.vpn.api.shared.support.SupportFAQFilters
import com.mooncloak.vpn.api.shared.token.RevokeTokenRequestBody
import com.mooncloak.vpn.api.shared.token.RevokedTokenResponseBody
import com.mooncloak.vpn.api.shared.token.Token
import com.mooncloak.vpn.api.shared.token.TokenTypeHint
import com.mooncloak.vpn.api.shared.token.TransactionToken
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration

@OptIn(ExperimentalApixApi::class)
public class MooncloakVpnServiceHttpApi public constructor(
    private val httpClient: HttpClient,
    private val hostUrlProvider: HostUrlProvider
) : VpnServiceApi {

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getReflection(
        connectionTimeout: Duration?,
        socketTimeout: Duration?,
        requestTimeout: Duration?
    ): HttpReflection = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get(url("/mirror")) {
            headers {
                // Try to force the reflection request to not cache. We should always get back the fresh value from
                // this API endpoint. Really it is up to the server if it includes cache headers, but the client can
                // indicate that we don't want cached values.
                append("Cache-Control", "no-cache")
                append("Pragma", "no-cache")
            }

            timeout {
                connectTimeoutMillis = connectionTimeout?.inWholeMilliseconds
                socketTimeoutMillis = socketTimeout?.inWholeMilliseconds
                requestTimeoutMillis = requestTimeout?.inWholeMilliseconds
            }
        }

        return@withContext response.body<HttpResponseBody<HttpReflection>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getAvailablePlans(): AvailablePlans = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get(url("/billing/plans"))

        return@withContext response.body<HttpResponseBody<AvailablePlans>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getPlan(id: String): Plan = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get(url("/billing/plan/$id"))

        return@withContext response.body<HttpResponseBody<Plan>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getPaymentInvoice(
        planId: String,
        secret: String?,
        token: Token?
    ): BitcoinPlanInvoice = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/payment/invoice")) {
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
    override suspend fun getPaymentStatus(
        paymentId: String,
        token: TransactionToken,
        secret: String?
    ): PlanPaymentStatus = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/payment/status")) {
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
    override suspend fun exchangeToken(
        receipt: ProofOfPurchase,
        token: Token?
    ): ServiceTokens = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/token/exchange")) {
            if (token != null) {
                bearerAuth(token.value)
            }

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(receipt)
        }

        return@withContext response.body<HttpResponseBody<ServiceTokens>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun refreshToken(
        refreshToken: Token
    ): ServiceTokens = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/token/refresh")) {
            bearerAuth(refreshToken.value)
        }

        return@withContext response.body<HttpResponseBody<ServiceTokens>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun revokeToken(
        refreshToken: Token
    ): Boolean = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/token/revoke")) {
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
    override suspend fun getCurrentSubscription(
        token: Token
    ): ServiceSubscription = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/subscription")) {
            bearerAuth(token.value)
        }

        return@withContext response.body<HttpResponseBody<ServiceSubscription>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getCurrentSubscriptionUsage(
        token: Token
    ): ServiceSubscriptionUsage = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/subscription/usage")) {
            bearerAuth(token.value)
        }

        return@withContext response.body<HttpResponseBody<ServiceSubscriptionUsage>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getCountry(
        code: CountryCode,
        token: Token?
    ): CountryDetails = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get(url("/vpn/service/country/${code.value}")) {
            token?.let { bearerAuth(it.value) }
        }

        return@withContext response.body<HttpResponseBody<CountryDetails>>().getOrThrow()
    }

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    override suspend fun paginateCountries(
        token: Token?,
        direction: Direction,
        cursor: Cursor?,
        count: UInt,
        sort: SortOptions?,
        filters: CountryFilters?
    ): CountryPage = withContext(Dispatchers.PlatformIO) {
        val pageRequest = PageRequest<String, CountryFilters>(
            data = null,
            direction = direction,
            cursor = cursor,
            count = count,
            sort = sort,
            filters = filters
        )

        val response = httpClient.post(url("/vpn/service/countries")) {
            token?.let { bearerAuth(it.value) }

            contentType(ContentType.Application.Json)

            setBody(pageRequest)
        }

        return@withContext response.body<HttpResponseBody<CountryPage>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun registerClient(
        serverId: String,
        clientPublicKey: Base64Key,
        token: Token?
    ): RegisteredClient = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.post(url("/vpn/service/client/register")) {
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
    override suspend fun getServer(
        id: String,
        token: Token?
    ): Server = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get(url("/vpn/service/server/$id")) {
            token?.value?.let { bearerAuth(it) }
        }

        return@withContext response.body<HttpResponseBody<Server>>().getOrThrow()
    }

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    override suspend fun paginateServers(
        query: String?,
        token: Token?,
        direction: Direction,
        cursor: Cursor?,
        count: UInt,
        sort: SortOptions?,
        filters: ServerFilters?
    ): ResolvedPage<Server> = withContext(Dispatchers.PlatformIO) {
        val pageRequest = PageRequest(
            data = query,
            direction = direction,
            cursor = cursor,
            count = count,
            sort = sort,
            filters = filters
        )

        val response = httpClient.post(url("/vpn/service/servers")) {
            token?.value?.let { bearerAuth(it) }

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(pageRequest)
        }

        return@withContext response.body<HttpResponseBody<ResolvedPage<Server>>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getContributors(): CurrentContributors = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get(url("/vpn/app/contributor"))

        return@withContext response.body<HttpResponseBody<CurrentContributors>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getContributor(id: String): Contributor = withContext(Dispatchers.PlatformIO) {
        val response = httpClient.get(url("/vpn/app/contributor/$id"))

        return@withContext response.body<HttpResponseBody<Contributor>>().getOrThrow()
    }

    @OptIn(ExperimentalPaginationAPI::class)
    @Throws(ApiException::class, CancellationException::class)
    override suspend fun getSupportFAQPages(
        token: Token?,
        filters: SupportFAQFilters?
    ): List<FAQPage> {
        val pageRequest = PageRequest<String, SupportFAQFilters>(
            filters = filters
        )

        val response = httpClient.post(url("/support/faqs")) {
            token?.value?.let { bearerAuth(it) }

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)

            setBody(pageRequest)
        }

        val pages = response.body<HttpResponseBody<ResolvedPage<FAQPage>>>().getOrThrow()

        return pages.items
    }

    private suspend fun url(vararg path: String, encodeSlash: Boolean = false): Url {
        val base = hostUrlProvider.get()

        return URLBuilder(base).apply {
            this.appendPathSegments(components = path, encodeSlash = encodeSlash)
        }.build()
    }
}
