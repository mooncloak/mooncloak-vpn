package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.app.shared.settings.SubscriptionSettings
import com.mooncloak.vpn.crypto.lunaris.model.GiftedCryptoToken
import com.mooncloak.vpn.crypto.lunaris.repository.GiftedCryptoTokenRepository
import kotlinx.datetime.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class RequestLunarisGiftUseCase @Inject public constructor(
    private val giftedCryptoTokenRepository: GiftedCryptoTokenRepository,
    private val api: VpnServiceApi,
    private val subscriptionSettings: SubscriptionSettings,
    private val clock: Clock
) {

    @OptIn(ExperimentalUuidApi::class)
    public suspend operator fun invoke(address: String): GiftedCryptoToken? {
        val response = api.requestLunarisGift(
            token = subscriptionSettings.tokens.get()?.accessToken,
            address = address
        )

        return if (response.amount.value != BigDecimal.ZERO) {
            val now = clock.now()
            val id = Uuid.random().toHexString()

            giftedCryptoTokenRepository.insert(id) {
                GiftedCryptoToken(
                    id = id,
                    created = now,
                    updated = now,
                    gifted = response.timestamp,
                    address = response.address,
                    amount = response.amount,
                    promoCode = response.promoCode,
                    message = response.message
                )
            }
        } else {
            null
        }
    }
}
