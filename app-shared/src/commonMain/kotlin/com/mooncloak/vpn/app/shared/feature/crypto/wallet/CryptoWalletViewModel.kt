package com.mooncloak.vpn.app.shared.feature.crypto.wallet

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.app.shared.model.NotificationStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_blockchain_ethereum
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_value_network_polygon
import com.mooncloak.vpn.app.shared.resource.global_unexpected_error
import com.mooncloak.vpn.crypto.lunaris.CryptoWalletManager
import com.mooncloak.vpn.crypto.lunaris.model.CryptoWallet
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString

@Stable
public class CryptoWalletViewModel @Inject public constructor(
    private val cryptoWalletManager: CryptoWalletManager,
    private val vpnServiceApi: VpnServiceApi
) : ViewModel<CryptoWalletStateModel>(initialStateValue = CryptoWalletStateModel()) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                var blockChain: String? = null
                var network: String? = null
                var wallet: CryptoWallet? = null

                try {
                    emit { current -> current.copy(isLoading = true) }

                    blockChain = getString(Res.string.crypto_wallet_value_blockchain_ethereum)
                    network = getString(Res.string.crypto_wallet_value_network_polygon)
                    wallet = cryptoWalletManager.getDefaultWallet()

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            blockChain = blockChain,
                            network = network,
                            wallet = wallet
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error loading Lunaris Wallet.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            blockChain = blockChain,
                            network = network,
                            wallet = wallet,
                            error = NotificationStateModel(
                                message = getString(Res.string.global_unexpected_error)
                            )
                        )
                    }
                }
            }
        }
    }

    public fun refresh() {
        // TODO
    }
}
