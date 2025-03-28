package com.mooncloak.vpn.app.shared.api.billing

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.billing.ProofOfPurchase
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository

public class ExchangeProofOfPurchaseForServiceTokensUseCase @Inject public constructor(
    private val api: VpnServiceApi,
    private val serviceTokensRepository: ServiceTokensRepository,
) {

    public suspend operator fun invoke(proof: ProofOfPurchase): ServiceTokens {
        val currentTokens = serviceTokensRepository.getLatest()

        val tokens = api.exchangeToken(
            receipt = proof,
            token = currentTokens?.accessToken
        )

        serviceTokensRepository.add(tokens)

        return tokens
    }
}
