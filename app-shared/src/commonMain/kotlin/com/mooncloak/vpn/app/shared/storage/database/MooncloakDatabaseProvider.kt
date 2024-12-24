package com.mooncloak.vpn.app.shared.storage.database

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.storage.database.adapter.DatabaseAdapter
import com.mooncloak.vpn.app.shared.storage.database.adapter.instantAsMillisecondsLong
import com.mooncloak.vpn.app.shared.storage.database.adapter.jsonElementAsString
import com.mooncloak.vpn.app.shared.storage.database.adapter.jsonObjectAsString
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.app.storage.sqlite.database.PurchaseReceipt
import com.mooncloak.vpn.app.storage.sqlite.database.ServicePlan
import com.mooncloak.vpn.app.storage.sqlite.database.ServiceTokens

public class MooncloakDatabaseProvider @Inject public constructor(
    private val databaseDriverFactory: DatabaseDriverFactory
) {

    public fun get(): MooncloakDatabase = MooncloakDatabase(
        driver = databaseDriverFactory.create(),
        PurchaseReceiptAdapter = PurchaseReceipt.Adapter(
            createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
            updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
            purchasedAdapter = DatabaseAdapter.instantAsMillisecondsLong()
        ),
        ServicePlanAdapter = ServicePlan.Adapter(
            createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
            updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
            breakdownAdapter = DatabaseAdapter.jsonElementAsString(),
            metadataAdapter = DatabaseAdapter.jsonObjectAsString(),
            subscriptionAdapter = DatabaseAdapter.jsonElementAsString(),
            trialAdapter = DatabaseAdapter.jsonElementAsString()
        ),
        ServiceTokensAdapter = ServiceTokens.Adapter(
            createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
            updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
            issuedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
            expirationAdapter = DatabaseAdapter.instantAsMillisecondsLong()
        )
    )
}
