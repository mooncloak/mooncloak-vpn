package com.mooncloak.vpn.app.shared.feature.wireguard.dns

import com.mooncloak.kodetools.konstruct.annotations.Component
import com.mooncloak.kodetools.konstruct.annotations.Provides
import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent

@Component
@FeatureScoped
internal abstract class DnsServerConfigComponent internal constructor(
    @Component internal val applicationComponent: ApplicationComponent,
    @Component internal val presentationComponent: PresentationComponent,
    @get:Provides internal val listener: DnsServerOnSaveCompleteListener
) : FeatureDependencies {

    abstract override val viewModel: DnsServerConfigViewModel
}

internal expect fun FeatureDependencies.Companion.createDnsServerConfigComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    listener: DnsServerOnSaveCompleteListener
): DnsServerConfigComponent
