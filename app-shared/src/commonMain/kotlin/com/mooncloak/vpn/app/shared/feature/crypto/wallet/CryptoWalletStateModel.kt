package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.runtime.Immutable
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.PromoDetails
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.RestoreCryptoStateModel
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.SendCryptoStateModel
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletBalance
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletFeedItem
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletStatDetails
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import kotlinx.datetime.Instant

@Immutable
public data class CryptoWalletStateModel public constructor(
    public val blockChain: String? = null,
    public val network: String? = null,
    public val timestamp: Instant? = null,
    public val wallet: CryptoWallet? = null,
    public val balance: WalletBalance? = null,
    public val promo: PromoDetails? = null,
    public val stats: WalletStatDetails? = null,
    public val send: SendCryptoStateModel = SendCryptoStateModel(),
    public val restore: RestoreCryptoStateModel = RestoreCryptoStateModel(),
    public val secureRecoveryPhrase: String? = null,
    public val secureRecoveryPhraseVisible: Boolean = false,
    public val items: List<WalletFeedItem> = emptyList(),
    public val uniSwapUri: String? = null,
    public val isLoading: Boolean = false,
    public val isCreatingWallet: Boolean = false,
    public val error: NotificationStateModel? = null,
    public val success: NotificationStateModel? = null
)

public val CryptoWalletStateModel.sendEnabled: Boolean
    inline get() = !this.isLoading && this.wallet != null

public val CryptoWalletStateModel.receiveEnabled: Boolean
    inline get() = !this.isLoading && this.wallet != null

public val CryptoWalletStateModel.revealEnabled: Boolean
    inline get() = !this.isLoading && this.wallet != null

public val CryptoWalletStateModel.showNoWalletCard: Boolean
    inline get() = !this.isLoading && this.wallet == null
