package com.mooncloak.vpn.crypto.lunaris.model

import com.ionspin.kotlin.bignum.integer.BigInteger

/**
 * Sealed class representing the result of a [send] operation.
 */
public sealed class SendResult {

    /**
     * Indicates a successful transaction.
     *
     * @property txHash The transaction hash as a hexadecimal string.
     * @property gasUsed The amount of gas used by the transaction, if available, as a [BigInteger].
     */
    public data class Success public constructor(
        public val txHash: String,
        public val gasUsed: BigInteger? = null
    ) : SendResult()

    /**
     * Indicates a failed transaction attempt.
     *
     * @property errorMessage A description of the failure reason.
     */
    public data class Failure public constructor(
        public val errorMessage: String
    ) : SendResult()

    /**
     * Indicates the transaction is pending submission or confirmation.
     */
    public data class Pending public constructor(
        public val txHash: String? = null
    ) : SendResult()
}
