package com.mooncloak.vpn.app.shared.api

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.kodetools.konstruct.annotations.Inject
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
    ): PlanPaymentInfo {
        val response = httpClient.post("https://mooncloak.com/api/vpn/payment/info") {
            setBody(
                GetPaymentInfoRequestBody(
                    planId = planId,
                    secret = secret
                )
            )
        }

        return response.body<HttpResponseBody<PlanPaymentInfo>>().getOrThrow()
    }

    @Throws(ApiException::class, CancellationException::class)
    public suspend fun getPaymentStatus(
        paymentId: String,
        token: TransactionToken,
        secret: String? = null
    ): PaymentStatus {
        val response = httpClient.post("https://mooncloak.com/api/vpn/payment/status") {
            bearerAuth(token.value)

            setBody(
                GetPaymentStatusRequestBody(
                    paymentId = paymentId,
                    secret = secret
                )
            )
        }

        return response.body<HttpResponseBody<PaymentStatus>>().getOrThrow()
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
}
