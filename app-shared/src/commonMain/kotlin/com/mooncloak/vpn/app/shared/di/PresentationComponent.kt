package com.mooncloak.vpn.app.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.api.plan.DefaultServicePlansProvider
import com.mooncloak.vpn.api.shared.plan.ServicePlansProvider

@PresentationScoped
public abstract class PresentationComponent : PresentationDependencies {

    @Provides
    @PresentationScoped
    public fun provideVPNServicePlansProvider(provider: DefaultServicePlansProvider): ServicePlansProvider =
        provider

    public companion object
}

/**
 * Provides access to the [PresentationComponent] from [Composable] functions.
 */
internal val LocalPresentationComponent: ProvidableCompositionLocal<PresentationComponent> =
    staticCompositionLocalOf { error("No PresentationComponent was provided.") }
