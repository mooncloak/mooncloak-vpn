package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

@PresentationScoped
public abstract class PresentationComponent : PresentationDependencies {

    public companion object
}

/**
 * Provides access to the [PresentationComponent] from [Composable] functions.
 */
internal val LocalPresentationComponent: ProvidableCompositionLocal<PresentationComponent> =
    staticCompositionLocalOf { error("No PresentationComponent were provided.") }
