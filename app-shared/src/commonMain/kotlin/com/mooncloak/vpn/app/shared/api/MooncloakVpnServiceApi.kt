package com.mooncloak.vpn.app.shared.api

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ApiResponseBody
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.kodetools.konstruct.annotations.Inject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalApixApi::class)
public class MooncloakVpnServiceApi @Inject public constructor(
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
}
