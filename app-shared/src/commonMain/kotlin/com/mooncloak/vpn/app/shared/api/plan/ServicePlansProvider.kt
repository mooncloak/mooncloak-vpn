package com.mooncloak.vpn.app.shared.api.plan

import kotlinx.coroutines.flow.Flow

public interface ServicePlansProvider {

    public fun getPlansFlow(): Flow<List<Plan>>

    public companion object
}
