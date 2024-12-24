package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.kodetools.statex.update
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(ExperimentalPersistentStateAPI::class)
public class ServiceTokensSource @Inject public constructor(
    private val databaseSource: ServiceTokensDatabaseSource,
    private val subscriptionStorage: SubscriptionStorage
) : ServiceTokensRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun getLatest(): ServiceTokens? {
        subscriptionStorage.tokens.current.value?.let { return it }

        val latest = databaseSource.getLatest()

        subscriptionStorage.tokens.update(latest)

        return latest
    }

    override suspend fun get(id: String): ServiceTokens {
        val latest = subscriptionStorage.tokens.current.value

        if (latest != null && latest.id == id) {
            return latest
        }

        return databaseSource.get(id = id)
    }

    override suspend fun add(tokens: ServiceTokens) {
        mutex.withLock {
            databaseSource.add(tokens)

            val latest = databaseSource.getLatest() ?: tokens

            subscriptionStorage.tokens.update(latest)
        }
    }

    override suspend fun remove(id: String) {
        mutex.withLock {
            databaseSource.remove(id)

            if (id == subscriptionStorage.tokens.current.value?.id) {
                val latest = databaseSource.getLatest()

                subscriptionStorage.tokens.update(latest)
            }
        }
    }
}
