package com.mooncloak.vpn.crypto.lunaris.model

import com.mooncloak.vpn.api.shared.currency.Currency

/**
 * Data class representing a single transaction in the wallet's history.
 *
 * @property [txHash] The transaction hash as a hexadecimal string.
 *
 * @property [origin] The sender's address as a hexadecimal string.
 *
 * @property [target] The recipient's address as a hexadecimal string.
 *
 * @property [amount] The amount of LNRS transferred, as a [Currency.Amount].
 *
 * @property [status] The current status of the transaction (e.g., PENDING, CONFIRMED).
 */
public data class CryptoTransaction public constructor(
    public val txHash: String,
    public val origin: String,
    public val target: String,
    public val amount: Currency.Amount,
    public val status: TransactionStatus
)
