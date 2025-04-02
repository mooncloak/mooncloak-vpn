package com.mooncloak.vpn.crypto.lunaris

/**
 * Enum for filtering transaction history by type.
 */
public enum class TransactionType {

    /** Transactions sent from the wallet. */
    SENT,

    /** Transactions received by the wallet. */
    RECEIVED,

    /** All transactions involving the wallet. */
    ALL
}
