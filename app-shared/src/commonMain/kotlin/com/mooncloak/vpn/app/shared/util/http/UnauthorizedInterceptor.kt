package com.mooncloak.vpn.app.shared.util.http

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.storage.AppStorage
import com.mooncloak.vpn.app.shared.storage.PreferencesStorage
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.Sender
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.url
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

/**
 * A component that is invoked to authorize a previously unauthorized HTTP call.
 */
public fun interface UnauthorizedInterceptor {

    /**
     * A function that performs the authorization request. The function takes two parameters: the original
     * [HttpRequestBuilder] [request] and the [original] [HttpClientCall]. The function should return an
     * [HttpClientCall] containing the response information from the authorization request. If an authorization request
     * should not be performed, this function can return the original [HttpClientCall] value.
     */
    public suspend fun Sender.invoke(request: HttpRequestBuilder, original: HttpClientCall): HttpClientCall

    public companion object
}

/**
 * Applies an interceptor to this [HttpClient] instance that will intercept any [HttpStatusCode.Unauthorized] responses
 * and perform the [authorize] task before optionally retrying the original request.
 *
 * @param [applies] A function that determines whether the provided [HttpRequestBuilder] request should be intercepted
 * if the response of the request is [HttpStatusCode.Unauthorized]. If this function returns `true`, the interceptor
 * will be applied, otherwise the interceptor will not be applied. This allows only applying the interceptor for
 * certain endpoints or conditions. Defaults to always applying the interceptor for every request.
 *
 * @param [retry] A function that determines whether the original request should be retried after the [authorize]
 * function was invoked to perform authorization. The function takes two parameters: the authorized [HttpClientCall]
 * and the original [HttpClientCall] in that order. If this function returns `true`, then the original request will be
 * retried, otherwise the original request will not be retried and the original response will be forwarded to the
 * pipeline. This allows conditionally applying retry logic. Defaults to retrying the original request if the
 * authorized request is considered successful according to the [HttpStatusCode.isSuccess] function.
 *
 * @param [authorize] An [UnauthorizedInterceptor] that performs the authorization request.
 *
 * @see [HttpSend] for more information about intercepting HTTP requests.
 */
public fun HttpClient.interceptUnauthorized(
    applies: (request: HttpRequestBuilder) -> Boolean = { true },
    retry: (authorized: HttpClientCall, original: HttpClientCall) -> Boolean = { authorized, _ -> authorized.response.status.isSuccess() },
    authorize: UnauthorizedInterceptor
) {
    this.plugin(HttpSend).intercept { request ->
        val originalCall = execute(request)

        if (applies.invoke(request) && originalCall.response.status == HttpStatusCode.Unauthorized) {
            val authorizeCall = authorize.run { invoke(request, originalCall) }

            if (retry.invoke(authorizeCall, originalCall)) {
                execute(request)
            } else {
                originalCall
            }
        } else {
            originalCall
        }
    }
}

public class DefaultUnauthorizedInterceptor @Inject public constructor(
    private val subscriptionStorage: SubscriptionStorage
) : UnauthorizedInterceptor {

    @OptIn(ExperimentalPersistentStateAPI::class)
    override suspend fun Sender.invoke(request: HttpRequestBuilder, original: HttpClientCall): HttpClientCall {
        val refreshToken = subscriptionStorage.tokens.current.value?.refreshToken

        return if (refreshToken != null && request.url.buildString().startsWith("https://mooncloak.com/api")) {
            request.url("https://mooncloak.com/api/vpn/token/refresh")
            request.bearerAuth(token = refreshToken.value)

            execute(request)
        } else {
            original
        }
    }
}
