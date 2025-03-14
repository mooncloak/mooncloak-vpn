package com.mooncloak.vpn.network.core

import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.data.shared.cache.Cache
import com.mooncloak.vpn.network.core.ip.DefaultPublicDeviceIPAddressProvider
import com.mooncloak.vpn.network.core.ip.PublicDeviceIPAddressProvider

public operator fun PublicDeviceIPAddressProvider.Companion.invoke(
    mooncloakApi: MooncloakVpnServiceHttpApi,
    cache: Cache
): PublicDeviceIPAddressProvider = DefaultPublicDeviceIPAddressProvider(
    mooncloakApi = mooncloakApi,
    cache = cache
)
