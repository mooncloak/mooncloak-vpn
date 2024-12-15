package com.mooncloak.vpn.app.shared.api.repository

import com.mooncloak.vpn.app.shared.api.ServiceAccessDetails

public interface ServiceAccessDetailsRepository {

    public suspend fun getLatest(): ServiceAccessDetails?

    public suspend fun add(details: ServiceAccessDetails)

    public companion object
}
