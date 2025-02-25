package com.mooncloak.vpn.app.shared.database

import app.cash.sqldelight.db.SqlDriver
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.data.shared.database.adapter.DatabaseAdapter
import com.mooncloak.vpn.data.shared.database.adapter.instantAsMillisecondsLong
import com.mooncloak.vpn.data.shared.database.adapter.jsonElementAsString
import com.mooncloak.vpn.data.shared.database.adapter.jsonObjectAsString
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.app.storage.sqlite.database.PurchaseReceipt
import com.mooncloak.vpn.app.storage.sqlite.database.RegisteredClient
import com.mooncloak.vpn.app.storage.sqlite.database.ServerConnectionRecord
import com.mooncloak.vpn.app.storage.sqlite.database.ServicePlan
import com.mooncloak.vpn.app.storage.sqlite.database.ServiceTokens
import com.mooncloak.vpn.data.shared.database.SqlDriverFactory
import com.mooncloak.vpn.data.shared.database.DatabaseManager

@Singleton
public class MooncloakDatabaseProvider @Inject public constructor(
    private val databaseDriverFactory: SqlDriverFactory
) : DatabaseManager<MooncloakDatabase> {

    private var cachedDriver: SqlDriver? = null
    private var cachedDatabase: MooncloakDatabase? = null

    public override fun get(): MooncloakDatabase {
        var database = cachedDatabase

        if (database == null) {
            cachedDriver?.close()
            val driver = databaseDriverFactory.create()
            cachedDriver = driver

            database = MooncloakDatabase(
                driver = driver,
                PurchaseReceiptAdapter = PurchaseReceipt.Adapter(
                    createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    purchasedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    priceAdapter = DatabaseAdapter.jsonElementAsString(),
                    plan_idsAdapter = DatabaseAdapter.jsonElementAsString()
                ),
                ServicePlanAdapter = ServicePlan.Adapter(
                    createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    metadataAdapter = DatabaseAdapter.jsonObjectAsString(),
                    subscriptionAdapter = DatabaseAdapter.jsonElementAsString(),
                    trialAdapter = DatabaseAdapter.jsonElementAsString(),
                    descriptionAdapter = DatabaseAdapter.jsonElementAsString(),
                    detailsAdapter = DatabaseAdapter.jsonElementAsString()
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

            cachedDatabase = database
        }

        return database
    }

    public override fun close() {
        cachedDatabase = null
        cachedDriver?.close()
        cachedDriver = null
    }
}
