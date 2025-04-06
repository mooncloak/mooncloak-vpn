package com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.SecretRecoveryPhraseCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_reveal
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_reveal
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_warning_srp
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RevealSeedPhraseLayout(
    sheetState: ManagedModalBottomSheetState,
    phrase: String?,
    phraseVisible: Boolean,
    onTogglePhraseVisibility: () -> Unit,
    onCopied: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipBoardManager = LocalClipboardManager.current

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.crypto_wallet_title_reveal),
            description = stringResource(Res.string.crypto_wallet_description_reveal)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = DefaultHorizontalPageSpacing)
            ) {
                WalletCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Default.PriorityHigh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onError
                        )

                        Text(
                            modifier = Modifier.padding(start = 16.dp),
                            text = stringResource(Res.string.crypto_wallet_warning_srp),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }

                SecretRecoveryPhraseCard(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 32.dp),
                    phrase = phrase ?: "",
                    visible = phraseVisible,
                    onToggleVisibility = onTogglePhraseVisibility,
                    onCopy = {
                        clipBoardManager.setText(AnnotatedString(phrase ?: ""))
                        onCopied.invoke()
                    },
                    onDownload = {
                        // TODO: Support downloading the phrase.
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
