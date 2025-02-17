package com.mooncloak.vpn.app.shared.feature.wireguard.dns.di

import com.mooncloak.vpn.app.shared.di.ApplicationComponent
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.di.FeatureDependencies
import com.mooncloak.vpn.app.shared.di.PresentationComponent
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.DnsServerConfigViewModel
import com.mooncloak.vpn.app.shared.feature.wireguard.dns.DnsServerOnSaveCompleteListener

@FeatureScoped
internal abstract class DnsServerConfigComponent internal constructor() : FeatureDependencies {

    abstract override val viewModel: DnsServerConfigViewModel
    abstract val listener: DnsServerOnSaveCompleteListener
}

internal expect fun FeatureDependencies.Companion.createDnsServerConfigComponent(
    applicationComponent: ApplicationComponent,
    presentationComponent: PresentationComponent,
    listener: DnsServerOnSaveCompleteListener
): DnsServerConfigComponent
