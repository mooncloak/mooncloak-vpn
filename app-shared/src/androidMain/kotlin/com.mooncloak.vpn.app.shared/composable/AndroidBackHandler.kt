package com.mooncloak.vpn.app.shared.composable

import androidx.compose.runtime.Composable

/**
 * A multiplatform variant of the [androidx.activity.compose.BackHandler] composable function. For platforms that don't
 * support this operation (currently every platform except for Android), this function performs no operation. This is a
 * convenience function for accessing back functionality in the common source set.
 */
@Composable
public actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(
        enabled = enabled,
        onBack = onBack
    )
}
