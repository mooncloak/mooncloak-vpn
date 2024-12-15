package com.mooncloak.vpn.app.shared.api.service

public interface ServiceAccessDetailsRepository {

    public suspend fun getLatest(): ServiceAccessDetails?

    public suspend fun add(details: ServiceAccessDetails)

    public companion object
}
