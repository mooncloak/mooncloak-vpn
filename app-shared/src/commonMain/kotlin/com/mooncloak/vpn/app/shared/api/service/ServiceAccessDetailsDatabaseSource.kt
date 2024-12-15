package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.kodetools.konstruct.annotations.Inject

internal class ServiceAccessDetailsDatabaseSource @Inject internal constructor(

): ServiceAccessDetailsRepository {

    override suspend fun getLatest(): ServiceAccessDetails? {
        TODO("Not yet implemented")
    }

    override suspend fun add(details: ServiceAccessDetails) {
        TODO("Not yet implemented")
    }
}
