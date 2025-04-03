package com.mooncloak.vpn.crypto.lunaris.model

/**
 * Represents an account for a crypto currency.
 *
 * @property [address] The address of the recipient.
 *
 * @property [name] The name, such as the ENS name, of the recipient if available. Ex: "chris.eth".
 */
public data class CryptoAccount public constructor(
    public val address: String,
    public val name: String? = null
)
