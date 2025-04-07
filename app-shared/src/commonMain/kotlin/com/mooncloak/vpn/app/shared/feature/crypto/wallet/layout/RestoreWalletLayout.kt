package com.mooncloak.vpn.app.shared.feature.crypto.wallet.layout

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.feature.crypto.wallet.model.isValid
import com.mooncloak.vpn.app.shared.model.TextFieldStateModel
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_action_restore_wallet
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_description_restore_wallet
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_label_srp
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_placeholder_srp
import com.mooncloak.vpn.app.shared.resource.crypto_wallet_title_restore_wallet
import com.mooncloak.vpn.app.shared.theme.DefaultHorizontalPageSpacing
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RestoreWalletLayout(
    sheetState: ManagedModalBottomSheetState,
    enabled: Boolean,
    phrase: TextFieldStateModel,
    onPhraseChanged: (value: TextFieldValue) -> Unit,
    onRestore: () -> Unit,
    modifier: Modifier = Modifier
) {
    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(Res.string.crypto_wallet_title_restore_wallet),
            description = stringResource(Res.string.crypto_wallet_description_restore_wallet)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = DefaultHorizontalPageSpacing)
            ) {
                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.crypto_wallet_label_srp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = SecondaryAlpha)
                )

                TextField(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 8.dp),
                    value = phrase.value,
                    onValueChange = onPhraseChanged,
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
                            text = stringResource(Res.string.crypto_wallet_placeholder_srp)
                        )
                    },
                    supportingText = (@Composable {
                        Text(
                            modifier = Modifier.padding(top = 4.dp)
                                .animateContentSize(),
                            text = phrase.error ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }).takeIf { phrase.error != null }
                )

                Button(
                    modifier = Modifier.padding(vertical = 32.dp)
                        .sizeIn(maxWidth = 400.dp)
                        .fillMaxWidth(),
                    enabled = enabled,
                    onClick = onRestore
                ) {
                    Text(
                        text = stringResource(Res.string.crypto_wallet_action_restore_wallet)
                    )
                }
            }
        }
    }
}
