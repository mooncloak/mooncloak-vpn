package com.mooncloak.vpn.api.shared.plan

import kotlinx.coroutines.flow.Flow

public interface ServicePlansProvider {

    public fun getPlansFlow(): Flow<List<Plan>>

    public companion object
}
