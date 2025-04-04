package com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.composable.WalletBalanceCard
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.SendCryptoStateModel
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.WalletBalance
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.isValid
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_send
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_send
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_amount
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_recipient_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_placeholder_address
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_placeholder_amount
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_send
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_warning_send
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SendPaymentLayout(
    balance: WalletBalance?,
    model: SendCryptoStateModel,
    onAddressChanged: (address: TextFieldValue) -> Unit,
    onAmountChanged: (amount: TextFieldValue) -> Unit,
    onSend: () -> Unit,
    sheetState: ManagedModalBottomSheetState,
    modifier: Modifier = Modifier
) {
    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.crypto_wallet_title_send),
            description = stringResource(Res.string.crypto_wallet_description_send)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = DefaultHorizontalPageSpacing)
            ) {
                WalletBalanceCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    cryptoAmount = balance?.amount?.formatted,
                    localEstimatedAmount = balance?.localEstimate?.formatted
                )

                Text(
                    modifier = Modifier.padding(top = 32.dp),
                    text = stringResource(Res.string.crypto_wallet_label_recipient_address),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                )

                TextField(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp),
                    value = model.address.value,
                    onValueChange = onAddressChanged,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    ),
                    shape = RoundedCornerShape(5.dp),
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.crypto_wallet_placeholder_address)
                        )
                    },
                    supportingText = (@Composable {
                        Text(
                            modifier = Modifier.padding(top = 4.dp)
                                .animateContentSize(),
                            text = model.address.error ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }).takeIf { model.address.error != null }
                )

                Text(
                    modifier = Modifier.padding(top = 32.dp),
                    text = stringResource(Res.string.crypto_wallet_label_amount),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                )

                TextField(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    ),
                    shape = RoundedCornerShape(5.dp),
                    value = model.amount.value,
                    onValueChange = onAmountChanged,
                    placeholder = {
                        Text(
                            text = stringResource(Res.string.crypto_wallet_placeholder_amount)
                        )
                    },
                    supportingText = (@Composable {
                        Text(
                            modifier = Modifier.padding(top = 4.dp)
                                .animateContentSize(),
                            text = model.amount.error ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }).takeIf { model.amount.error != null },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    )
                )

                model.estimatedGas?.let { estimatedGas ->
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = estimatedGas,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                    )
                }

                Button(
                    modifier = Modifier.padding(top = 32.dp)
                        .sizeIn(maxWidth = 400.dp)
                        .fillMaxWidth(),
                    enabled = model.isValid,
                    onClick = onSend
                ) {
                    Text(
                        text = stringResource(Res.string.crypto_wallet_action_send)
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
                        .fillMaxWidth(),
                    text = stringResource(Res.string.crypto_wallet_warning_send),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
