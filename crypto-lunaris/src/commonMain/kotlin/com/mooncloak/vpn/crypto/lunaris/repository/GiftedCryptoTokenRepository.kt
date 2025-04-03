package com.mooncloak.vpn.crypto.lunaris.repository

import com.mooncloak.vpn.crypto.lunaris.model.GiftedCryptoToken
import com.mooncloak.vpn.crypto.lunaris.model.GiftedCryptoTokenStats
import com.mooncloak.vpn.data.shared.repository.MutableRepository

public interface GiftedCryptoTokenRepository : MutableRepository<GiftedCryptoToken> {

    public suspend fun getStats(): GiftedCryptoTokenStats?

    public companion object
}
