package com.mooncloak.vpn.app.shared.util.http

import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.toResult
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.Sender
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.url
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A component that is invoked to authorize a previously unauthorized HTTP call.
 */
public fun interface InterceptorAuthorizer {

    /**
     * A function that performs the authorization request. The function takes two parameters: the original
     * [HttpRequestBuilder] [request] and the [original] [HttpClientCall]. The function should return an
     * [HttpClientCall] containing the response information from the authorization request. If an authorization request
     * should not be performed, this function can return the original [HttpClientCall] value.
     */
    public suspend fun Sender.authorize(request: HttpRequestBuilder, original: HttpClientCall): HttpClientCall

    public companion object
}

public fun interface InterceptorRetry {

    public suspend fun retry(authorized: HttpClientCall, original: HttpClientCall): Boolean

    public companion object
}

public fun interface InterceptorApplier {

    public suspend fun apply(request: HttpRequestBuilder): Boolean

    public companion object
}

public interface UnauthorizedInterceptor : InterceptorAuthorizer,
    InterceptorRetry,
    InterceptorApplier {

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
 * @param [authorize] An [InterceptorAuthorizer] that performs the authorization request.
 *
 * @see [HttpSend] for more information about intercepting HTTP requests.
 */
public fun HttpClient.interceptUnauthorized(
    applies: InterceptorApplier = InterceptorApplier { true },
    retry: InterceptorRetry = InterceptorRetry { authorized, _ -> authorized.response.status.isSuccess() },
    authorize: InterceptorAuthorizer
) {
    this.plugin(HttpSend).intercept { request ->
        val originalCall = execute(request)

        if (applies.apply(request) && originalCall.response.status == HttpStatusCode.Unauthorized) {
            val authorizeCall = authorize.run { authorize(request, originalCall) }

            if (retry.retry(authorizeCall, originalCall)) {
                execute(request)
            } else {
                originalCall
            }
        } else {
            originalCall
        }
    }
}

/**
 * Applies an interceptor to this [HttpClient] instance that will intercept any [HttpStatusCode.Unauthorized] responses
 * and perform the [UnauthorizedInterceptor.authorize] task before optionally retrying the original request.
 *
 * @param [interceptor] The [UnauthorizedInterceptor] to use to intercept the HTTP requests.
 *
 * @see [HttpSend] for more information about intercepting HTTP requests.
 */
@Suppress("NOTHING_TO_INLINE")
public inline fun HttpClient.interceptUnauthorized(
    interceptor: UnauthorizedInterceptor
) {
    this.interceptUnauthorized(
        applies = interceptor,
        retry = interceptor,
        authorize = interceptor
    )
}

@OptIn(ExperimentalApixApi::class)
public class DefaultUnauthorizedInterceptor @Inject public constructor(
    private val serviceTokensRepository: ServiceTokensRepository
) : UnauthorizedInterceptor {

    private var refreshMutex = Mutex(locked = false)

    override suspend fun Sender.authorize(request: HttpRequestBuilder, original: HttpClientCall): HttpClientCall {
        val refreshToken = serviceTokensRepository.getLatest()?.refreshToken

        return if (refreshToken != null && request.url.buildString().startsWith("https://mooncloak.com/api")) {
            refreshMutex.withLock {
                // If the tokens are the same, they weren't refreshed while we were waiting on the lock, so attempt to
                // refresh them.
                if (refreshToken == serviceTokensRepository.getLatest()?.refreshToken) {
                    request.url("https://mooncloak.com/api/vpn/token/refresh")

                    // First remove the existing Authorization header which would be set to the access token. We need to
                    // instead provide the refresh token. If we just call the bearerAuth function, it appends the tokens
                    // together.
                    request.headers.remove("Authorization")
                    request.bearerAuth(token = refreshToken.value)

                    val updated = execute(request)
                    val responseBody = updated.response.body<HttpResponseBody<ServiceTokens>>()
                    val result = responseBody.toResult()

                    if (result.isSuccess) {
                        serviceTokensRepository.add(result.getOrThrow())
                    } else {
                        LogPile.error(
                            message = "Error refreshing tokens.",
                            cause = result.exceptionOrNull()
                        )
                    }

                    updated
                } else {
                    // The tokens did not match since the original request failed. That means another request refreshed
                    // the token values while we were waiting on the mutex to unlock. So just return the original
                    // request, and make sure it is retried.
                    original
                }
            }
        } else {
            original
        }
    }

    override suspend fun retry(authorized: HttpClientCall, original: HttpClientCall): Boolean = true

    override suspend fun apply(request: HttpRequestBuilder): Boolean {
        val path = request.url.encodedPath

        return path.startsWith("/api") && !path.endsWith("/token/refresh")
    }
}
