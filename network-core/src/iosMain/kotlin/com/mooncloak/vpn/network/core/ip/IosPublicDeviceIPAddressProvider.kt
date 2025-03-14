package com.mooncloak.vpn.network.core.ip

import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.data.shared.cache.Cache

public operator fun PublicDeviceIPAddressProvider.Companion.invoke(
    mooncloakApi: MooncloakVpnServiceHttpApi,
    cache: Cache
): PublicDeviceIPAddressProvider = DefaultPublicDeviceIPAddressProvider(
    mooncloakApi = mooncloakApi,
    cache = cache
)
