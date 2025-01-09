package com.mooncloak.vpn.app.shared.storage.database

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.app.shared.storage.database.adapter.DatabaseAdapter
import com.mooncloak.vpn.app.shared.storage.database.adapter.instantAsMillisecondsLong
import com.mooncloak.vpn.app.shared.storage.database.adapter.jsonElementAsString
import com.mooncloak.vpn.app.shared.storage.database.adapter.jsonObjectAsString
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.app.storage.sqlite.database.PurchaseReceipt
import com.mooncloak.vpn.app.storage.sqlite.database.RegisteredClient
import com.mooncloak.vpn.app.storage.sqlite.database.ServerConnectionRecord
import com.mooncloak.vpn.app.storage.sqlite.database.ServicePlan
import com.mooncloak.vpn.app.storage.sqlite.database.ServiceTokens

@Singleton
public class MooncloakDatabaseProvider @Inject public constructor(
    private val databaseDriverFactory: DatabaseDriverFactory
) {

    private var cached: MooncloakDatabase? = null

    public fun get(): MooncloakDatabase {
        var database = cached

        if (database == null) {
            database = MooncloakDatabase(
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
                ),
                ServerConnectionRecordAdapter = ServerConnectionRecord.Adapter(
                    createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    connectedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    starredAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    connectionTypesAdapter = DatabaseAdapter.jsonElementAsString(),
                    protocolsAdapter = DatabaseAdapter.jsonElementAsString(),
                    tagsAdapter = DatabaseAdapter.jsonElementAsString(),
                    serverCreatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    serverUpdatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    countryAdapter = DatabaseAdapter.jsonElementAsString(),
                    regionAdapter = DatabaseAdapter.jsonElementAsString()
                ),
                RegisteredClientAdapter = RegisteredClient.Adapter(
                    createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    registeredAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    expirationAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    allowed_ipsAdapter = DatabaseAdapter.jsonElementAsString()
                )
            )

            cached = database
        }

        return database
    }
}
