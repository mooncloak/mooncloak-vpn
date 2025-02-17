package com.mooncloak.vpn.app.shared.feature.wireguard.dns.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.DnsServerConfigViewModel

@FeatureScoped
internal abstract class DnsServerConfigComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: DnsServerConfigViewModel
}

internal expect fun FeatureDependencies.Companion.createDnsServerConfigComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
): DnsServerConfigComponent
