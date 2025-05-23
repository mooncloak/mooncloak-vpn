package com.mooncloak.vpn.app.shared.api.service

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.service.ServiceTokens
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import com.mooncloak.vpn.api.shared.token.Token
import com.mooncloak.vpn.api.shared.token.TokenType
import com.mooncloak.vpn.app.shared.database.MooncloakDatabaseProvider
import com.mooncloak.vpn.util.shared.coroutine.ApplicationCoroutineScope
import com.mooncloak.vpn.util.shared.coroutine.PlatformIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

public class ServiceTokensDatabaseSource @Inject public constructor(
    private val databaseProvider: MooncloakDatabaseProvider,
    private val clock: Clock,
    private val applicationCoroutineScope: ApplicationCoroutineScope
) : ServiceTokensRepository {

    private val mutex = Mutex(locked = false)

    private val latestFlow = getLatestSharedFlow()

    override suspend fun getLatest(): ServiceTokens? =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            return@withContext database.serviceTokensQueries.selectLatest()
                .executeAsOneOrNull()
                ?.toServiceTokens()
        }

    override fun latestFlow(): Flow<ServiceTokens?> = latestFlow

    override suspend fun get(id: String): ServiceTokens =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            return@withContext database.serviceTokensQueries.selectById(id = id)
                .executeAsOneOrNull()
                ?.toServiceTokens()
                ?: throw NoSuchElementException("No ServiceTokens found with id '$id'.")
        }

    override suspend fun get(count: Int, offset: Int): List<ServiceTokens> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            return@withContext database.serviceTokensQueries.selectPage(
                count = count.toLong(),
                offset = offset.toLong()
            ).executeAsList()
                .map { it.toServiceTokens() }
        }

    override suspend fun getAll(): List<ServiceTokens> =
        withContext(Dispatchers.PlatformIO) {
            val database = databaseProvider.get()

            return@withContext database.serviceTokensQueries.selectAll()
                .executeAsList()
                .map { it.toServiceTokens() }
        }

    override suspend fun insert(id: String, value: () -> ServiceTokens): ServiceTokens =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                val tokens = value.invoke()

                require(id == tokens.id) { "Provided id '$id' and ServiceTokens.id '${tokens.id}' does not match." }

                val now = clock.now()

                database.serviceTokensQueries.insert(
                    databaseId = null,
                    id = tokens.id,
                    created = now,
                    updated = now,
                    issued = tokens.issued,
                    expiration = tokens.expiration,
                    accessToken = tokens.accessToken.value,
                    type = tokens.tokenType.value,
                    refreshToken = tokens.refreshToken?.value,
                    scope = tokens.scopeString,
                    userId = tokens.userId
                )

                return@withContext tokens
            }
        }

    override suspend fun update(id: String, update: ServiceTokens.() -> ServiceTokens): ServiceTokens =
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.transactionWithResult {
                    val current = database.serviceTokensQueries.selectById(id = id)
                        .executeAsOneOrNull()
                        ?.toServiceTokens()
                        ?: throw NoSuchElementException("No ServiceTokens found with id '$id'.")

                    val updated = current.update()
                    val now = clock.now()

                    require(id == updated.id) { "Provided id '$id' and updated ServiceTokens.id '${updated.id}' does not match." }

                    database.serviceTokensQueries.updateById(
                        id = id,
                        updated = now,
                        issued = updated.issued,
                        expiration = updated.expiration,
                        accessToken = updated.accessToken.value,
                        type = updated.tokenType.value,
                        refreshToken = updated.refreshToken?.value,
                        scope = updated.scopeString,
                        userId = updated.userId
                    )

                    updated
                }
            }
        }

    override suspend fun remove(id: String) {
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.serviceTokensQueries.deleteById(id = id)
            }
        }
    }

    override suspend fun clear() {
        withContext(Dispatchers.PlatformIO) {
            mutex.withLock {
                val database = databaseProvider.get()

                database.serviceTokensQueries.deleteAll()
            }
        }
    }

    private fun com.mooncloak.vpn.data.sqlite.database.ServiceTokens.toServiceTokens(): ServiceTokens =
        ServiceTokens(
            id = this.id,
            accessToken = Token(value = this.accessToken),
            tokenType = TokenType(value = this.type),
            expiration = this.expiration,
            expiresIn = expiration - clock.now(),
            refreshToken = this.refreshToken?.let { Token(value = it) },
            scopeString = this.scope,
            issued = this.issued,
            userId = this.userId
        )

    private fun getLatestSharedFlow(): SharedFlow<ServiceTokens?> {
        val database = databaseProvider.get()

        return database.serviceTokensQueries.selectLatest()
            .asFlow()
            .mapToOneOrNull(applicationCoroutineScope.coroutineContext)
            .map { it?.toServiceTokens() }
            .shareIn(
                applicationCoroutineScope,
                started = SharingStarted.Lazily,
                replay = 1
            )
    }
}
