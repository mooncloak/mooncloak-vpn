package com.mooncloak.vpn.app.shared.database

import app.cash.sqldelight.db.SqlDriver
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.konstruct.annotations.Singleton
import com.mooncloak.vpn.data.sqlite.adapter.DatabaseAdapter
import com.mooncloak.vpn.data.sqlite.adapter.instantAsMillisecondsLong
import com.mooncloak.vpn.data.sqlite.adapter.jsonElementAsString
import com.mooncloak.vpn.data.sqlite.adapter.jsonObjectAsString
import com.mooncloak.vpn.data.sqlite.DatabaseManager
import com.mooncloak.vpn.data.sqlite.SqlDriverFactory
import com.mooncloak.vpn.data.sqlite.database.Crypto_address
import com.mooncloak.vpn.data.sqlite.database.Crypto_wallet
import com.mooncloak.vpn.data.sqlite.database.Gifted_crypto_token
import com.mooncloak.vpn.data.sqlite.database.MooncloakDatabase
import com.mooncloak.vpn.data.sqlite.database.PurchaseReceipt
import com.mooncloak.vpn.data.sqlite.database.RegisteredClient
import com.mooncloak.vpn.data.sqlite.database.ServerConnectionRecord
import com.mooncloak.vpn.data.sqlite.database.ServicePlan
import com.mooncloak.vpn.data.sqlite.database.ServiceTokens

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
                ),
                crypto_walletAdapter = Crypto_wallet.Adapter(
                    createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong()
                ),
                gifted_crypto_tokenAdapter = Gifted_crypto_token.Adapter(
                    createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    giftedAdapter = DatabaseAdapter.instantAsMillisecondsLong()
                ),
                crypto_addressAdapter = Crypto_address.Adapter(
                    createdAdapter = DatabaseAdapter.instantAsMillisecondsLong(),
                    updatedAdapter = DatabaseAdapter.instantAsMillisecondsLong()
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
