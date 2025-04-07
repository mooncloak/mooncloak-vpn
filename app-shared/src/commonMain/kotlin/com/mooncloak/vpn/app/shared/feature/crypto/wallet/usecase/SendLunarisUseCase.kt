package com.mooncloak.vpn.app.shared.feature.crypto.wallet.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.locale.ExperimentalLocaleApi
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.validation.LunarisAddressValidator
import com.mooncloak.vpn.crypto.lunaris.CryptoPasswordManager
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.getCurrentOrGenerate
import com.mooncloak.vpn.crypto.lunaris.model.SendResult
import com.mooncloak.vpn.util.shared.currency.Currency
import com.mooncloak.vpn.util.shared.currency.CurrencyAmountValidator
import com.mooncloak.vpn.util.shared.currency.lunarisValidator

@OptIn(ExperimentalLocaleApi::class)
public class SendLunarisUseCase @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val cryptoPasswordManager: CryptoPasswordManager,
    private val addressValidator: LunarisAddressValidator = LunarisAddressValidator(),
    private val amountValidator: CurrencyAmountValidator = Currency.Amount.lunarisValidator()
) {

    public suspend operator fun invoke(
        origin: String,
        target: String,
        amount: String
    ): SendResult {
        require(addressValidator.validate(origin).isSuccess)
        require(addressValidator.validate(target).isSuccess)

        val amountValidationResult = amountValidator.validate(amount)
        val amountValue = amountValidationResult.getOrThrow()
        val password = cryptoPasswordManager.getCurrentOrGenerate()

        return cryptoWalletManager.send(
            origin = origin,
            password = password,
            target = target,
            amount = amountValue
        )
    }
}
