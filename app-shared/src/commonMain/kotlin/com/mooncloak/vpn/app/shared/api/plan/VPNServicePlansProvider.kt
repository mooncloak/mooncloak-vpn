package com.mooncloak.vpn.app.shared.api.plan

import kotlinx.coroutines.flow.Flow

public interface VPNServicePlansProvider {

    public fun getPlansFlow(): Flow<List<VPNServicePlan>>

    public companion object
}
