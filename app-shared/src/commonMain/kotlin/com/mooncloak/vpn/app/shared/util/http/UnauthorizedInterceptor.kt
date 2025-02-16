package com.mooncloak.vpn.app.shared.util.http

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.Sender
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

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
 * @param [authorize] A function that performs the authorization request. The function takes two parameters: the
 * original [HttpRequestBuilder] request and the original [HttpClientCall]. The function should return a
 * [HttpClientCall] containing the response information from the authorization request. If an authorization request
 * should not be performed, this function can return the original [HttpClientCall] value.
 *
 * @see [HttpSend] for more information about intercepting HTTP requests.
 */
public fun HttpClient.interceptUnauthorized(
    applies: (request: HttpRequestBuilder) -> Boolean = { true },
    retry: (authorized: HttpClientCall, original: HttpClientCall) -> Boolean = { authorized, _ -> authorized.response.status.isSuccess() },
    authorize: Sender.(request: HttpRequestBuilder, original: HttpClientCall) -> HttpClientCall
) {
    this.plugin(HttpSend).intercept { request ->
        val originalCall = execute(request)

        if (applies.invoke(request) && originalCall.response.status == HttpStatusCode.Unauthorized) {
            val authorizeCall = authorize(request, originalCall)

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
