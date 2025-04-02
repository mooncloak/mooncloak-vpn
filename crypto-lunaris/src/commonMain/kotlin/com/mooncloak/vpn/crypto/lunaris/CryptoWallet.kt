package com.mooncloak.vpn.crypto.lunaris

import com.mooncloak.vpn.api.shared.currency.Currency
import kotlinx.datetime.Instant

/**
 * Represents a cryptocurrency wallet with detailed information.
 *
 * @property [address] The public address of the cryptocurrency wallet (e.g., "0x...").
 *
 * @property [currency] The cryptocurrency supported by the wallet.
 *
 * @property [created] The [Instant] that the wallet was created.
 */
public data class CryptoWallet public constructor(
    public val address: String,
    public val currency: Currency,
    public val created: Instant
)
