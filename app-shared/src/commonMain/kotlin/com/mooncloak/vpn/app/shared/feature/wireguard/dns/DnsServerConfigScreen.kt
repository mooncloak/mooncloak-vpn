package com.mooncloak.vpn.app.shared.feature.wireguard.dns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.text.error
import com.mooncloak.vpn.app.shared.composable.BottomSheetLayout
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheet
import com.mooncloak.vpn.app.shared.composable.ManagedModalBottomSheetState
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.rememberFeatureDependencies
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.composable.DnsMoonShieldCard
import com.mooncloak.vpn.app.shared.resource.Res
import com.mooncloak.vpn.app.shared.resource.cd_action_reset
import com.mooncloak.vpn.app.shared.resource.settings_dns_servers_action_save
import com.mooncloak.vpn.app.shared.resource.settings_dns_servers_description
import com.mooncloak.vpn.app.shared.resource.settings_dns_servers_header
import com.mooncloak.vpn.app.shared.resource.settings_dns_servers_label_primary
import com.mooncloak.vpn.app.shared.resource.settings_dns_servers_label_secondary
import com.mooncloak.vpn.app.shared.theme.SecondaryAlpha
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DnsServerConfigScreen(
    sheetState: ManagedModalBottomSheetState,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val componentDependencies = rememberFeatureDependencies { applicationComponent, presentationComponent ->
        FeatureDependencies.createDnsServerConfigComponent(
            applicationComponent = applicationComponent,
            presentationComponent = presentationComponent,
            listener = { onSave.invoke() }
        )
    }
    val viewModel = remember { componentDependencies.viewModel }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    ManagedModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState
    ) {
        BottomSheetLayout(
            modifier = modifier,
            loadingState = derivedStateOf { viewModel.state.current.value.isLoading || viewModel.state.current.value.isSaving },
            title = stringResource(Res.string.settings_dns_servers_header),
            description = stringResource(Res.string.settings_dns_servers_description)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DnsMoonShieldCard(
                        modifier = Modifier.fillMaxWidth(),
                        active = viewModel.state.current.value.moonShieldEnabled,
                        onActiveChange = viewModel::toggleMoonShield
                    )

                    TextField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 32.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                            focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        ),
                        enabled = !viewModel.state.current.value.isLoading,
                        value = viewModel.state.current.value.primary.value,
                        onValueChange = viewModel::updatePrimary,
                        label = {
                            Text(
                                text = stringResource(Res.string.settings_dns_servers_label_primary)
                            )
                        },
                        supportingText = (@Composable {
                            Text(
                                text = viewModel.state.current.value.primary.error ?: "",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }).takeIf { viewModel.state.current.value.primary.error != null },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(40.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        viewModel.resetPrimary()
                                    }
                                    .padding(8.dp),
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(Res.string.cd_action_reset),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                            )
                        }
                    )

                    TextField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 16.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha),
                            focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                        ),
                        enabled = !viewModel.state.current.value.isLoading,
                        value = viewModel.state.current.value.secondary.value,
                        onValueChange = viewModel::updateSecondary,
                        label = {
                            Text(
                                text = stringResource(Res.string.settings_dns_servers_label_secondary)
                            )
                        },
                        supportingText = (@Composable {
                            Text(
                                text = viewModel.state.current.value.secondary.error ?: "",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }).takeIf { viewModel.state.current.value.secondary.error != null },
                        trailingIcon = {
                            Icon(
                                modifier = Modifier.size(40.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        viewModel.resetSecondary()
                                    }
                                    .padding(8.dp),
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(Res.string.cd_action_reset),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = SecondaryAlpha)
                            )
                        }
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth()
                            .padding(top = 32.dp),
                        enabled = viewModel.state.current.value.isValidated,
                        onClick = viewModel::save
                    ) {
                        Text(
                            text = stringResource(Res.string.settings_dns_servers_action_save)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
