package com.mooncloak.vpn.app.shared.util.navigation

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

internal val LocalNavController: ProvidableCompositionLocal<NavController> =
    staticCompositionLocalOf { error("No 'LocalNavController' provided.") }
