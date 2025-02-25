package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

public class ServiceTokensSource @Inject public constructor(
    private val databaseSource: ServiceTokensDatabaseSource,
    private val subscriptionStorage: SubscriptionStorage
) : ServiceTokensRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun getLatest(): ServiceTokens? =
        withContext(Dispatchers.IO) {
            subscriptionStorage.tokens.get()?.let { return@withContext it }

            val latest = databaseSource.getLatest()

            subscriptionStorage.tokens.set(latest)

            return@withContext latest
        }

    override suspend fun get(id: String): ServiceTokens =
        withContext(Dispatchers.IO) {
            val latest = subscriptionStorage.tokens.get()

            if (latest != null && latest.id == id) {
                return@withContext latest
            }

            return@withContext databaseSource.get(id = id)
        }

    override suspend fun add(tokens: ServiceTokens) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                databaseSource.add(tokens)

                val latest = databaseSource.getLatest() ?: tokens

                subscriptionStorage.tokens.set(latest)
            }
        }
    }

    override suspend fun remove(id: String) {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                databaseSource.remove(id)

                if (id == subscriptionStorage.tokens.get()?.id) {
                    val latest = databaseSource.getLatest()

                    subscriptionStorage.tokens.set(latest)
                }
            }
        }
    }

    override suspend fun clear() {
        // TODO
    }
}
