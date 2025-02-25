package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.app.shared.storage.SubscriptionSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

public class ServiceTokensSource @Inject public constructor(
    private val databaseSource: ServiceTokensDatabaseSource,
    private val subscriptionStorage: SubscriptionSettings
) : ServiceTokensRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun getLatest(): ServiceTokens? =
        withContext(Dispatchers.IO) {
            subscriptionStorage.tokens.get()?.let { return@withContext it }

            val latest = databaseSource.getLatest()

            subscriptionStorage.tokens.set(latest)

            return@withContext latest
        }

    override fun latestFlow(): Flow<ServiceTokens?> = databaseSource.latestFlow()

    override suspend fun get(id: String): ServiceTokens =
        withContext(Dispatchers.IO) {
            val latest = subscriptionStorage.tokens.get()

            if (latest != null && latest.id == id) {
                return@withContext latest
            }

            return@withContext databaseSource.get(id = id)
        }

    override suspend fun get(count: Int, offset: Int): List<ServiceTokens> =
        withContext(Dispatchers.IO) {
            databaseSource.get(count = count, offset = offset)
        }

    override suspend fun getAll(): List<ServiceTokens> =
        withContext(Dispatchers.IO) {
            databaseSource.getAll()
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

    override suspend fun insert(id: String, value: () -> ServiceTokens): ServiceTokens =
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val inserted = databaseSource.insert(id = id, value = value)

                val latest = databaseSource.getLatest() ?: inserted

                subscriptionStorage.tokens.set(latest)

                return@withContext inserted
            }
        }

    override suspend fun update(id: String, update: ServiceTokens.() -> ServiceTokens): ServiceTokens =
        withContext(Dispatchers.IO) {
            mutex.withLock {
                val updated = databaseSource.update(id = id, update = update)

                val latest = databaseSource.getLatest() ?: updated

                subscriptionStorage.tokens.set(latest)

                return@withContext updated
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
        withContext(Dispatchers.IO) {
            mutex.withLock {
                databaseSource.clear()

                subscriptionStorage.tokens.remove()
            }
        }
    }
}
