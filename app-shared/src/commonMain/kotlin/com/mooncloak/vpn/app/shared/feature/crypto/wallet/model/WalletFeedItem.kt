package com.mooncloak.vpn.app.shared.feature.crypto.wallet.model

import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.runtime.Immutable
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet

@Immutable
public sealed interface WalletFeedItem {

    public val key: String
    public val contentType: String
    public val span: StaggeredGridItemSpan

    @Immutable
    public data class BalanceItem public constructor(
        public val balance: WalletBalance?
    ) : WalletFeedItem {

        override val key: String = "Balance:$balance"
        override val contentType: String = "BalanceItem"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.FullLine
    }

    @Immutable
    public data class PromoItem public constructor(
        public val details: PromoDetails
    ) : WalletFeedItem {

        override val key: String = details.toString()
        override val contentType: String = "PromoItem"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.FullLine
    }

    @Immutable
    public data object NoWalletItem : WalletFeedItem {

        override val key: String = "NoWalletItem"
        override val contentType: String = "NoWalletItem"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.SingleLane
    }

    @Immutable
    public data class StatsItem public constructor(
        public val stats: WalletStatDetails? = null
    ) : WalletFeedItem {

        override val key: String = stats.toString()
        override val contentType: String = "WalletStats"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.SingleLane
    }

    @Immutable
    public data object ActionsItem : WalletFeedItem {

        override val key: String = "WalletActionsItem"
        override val contentType: String = "WalletActionsItem"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.FullLine
    }

    @Immutable
    public data class AccountAddressItem public constructor(
        public val wallet: CryptoWallet
    ) : WalletFeedItem {

        override val key: String = "AccountAddress:${wallet.address}"
        override val contentType: String = "AccountAddressItem"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.FullLine
    }

    @Immutable
    public data class DetailsItem public constructor(
        public val wallet: CryptoWallet?,
        public val balance: WalletBalance?,
        public val blockChain: String?,
        public val network: String?,
        public val timestamp: String?
    ) : WalletFeedItem {

        override val key: String = "Details:${wallet?.address}"
        override val contentType: String = "DetailsItem"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.FullLine
    }

    @Immutable
    public data class TradeOnUniswap public constructor(
        public val uri: String
    ) : WalletFeedItem {

        override val key: String = "TradeOnUniswap:$uri"
        override val contentType: String = "TradeOnUniswap"
        override val span: StaggeredGridItemSpan = StaggeredGridItemSpan.FullLine
    }
}
