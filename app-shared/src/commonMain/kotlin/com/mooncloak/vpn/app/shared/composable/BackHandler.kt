package com.mooncloak.vpn.app.shared.composable

import androidx.compose.runtime.Composable

@Composable
public expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)
