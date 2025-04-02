package com.mooncloak.vpn.crypto.lunaris

/**
 * Enum representing the possible statuses of a transaction.
 */
public enum class TransactionStatus {

    /** The transaction has been submitted but not yet mined. */
    PENDING,

    /** The transaction has been mined and confirmed on the blockchain. */
    CONFIRMED,

    /** The transaction failed (e.g., reverted or out of gas). */
    FAILED
}
