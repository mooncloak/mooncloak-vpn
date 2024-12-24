package com.mooncloak.vpn.app.shared.api.service

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.token.Token
import com.mooncloak.vpn.app.shared.api.token.TokenType
import com.mooncloak.vpn.app.storage.sqlite.database.MooncloakDatabase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class ServiceTokensDatabaseSource @Inject public constructor(
    private val database: MooncloakDatabase,
    private val clock: Clock
) : ServiceTokensRepository {

    private val mutex = Mutex(locked = false)

    override suspend fun getLatest(): ServiceTokens? =
        database.serviceTokensQueries.selectLatest()
            .executeAsOneOrNull()
            ?.toServiceTokens()

    override suspend fun get(id: String): ServiceTokens =
        database.serviceTokensQueries.selectById(id = id)
            .executeAsOneOrNull()
            ?.toServiceTokens()
            ?: throw NoSuchElementException("No ServiceTokens found with id '$id'.")

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun add(tokens: ServiceTokens) {
        mutex.withLock {
            val now = clock.now()

            database.serviceTokensQueries.insert(
                databaseId = null,
                id = Uuid.random().toHexString(),
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
        }
    }

    override suspend fun remove(id: String) {
        mutex.withLock {
            database.serviceTokensQueries.deleteById(id = id)
        }
    }

    private fun com.mooncloak.vpn.app.storage.sqlite.database.ServiceTokens.toServiceTokens(): ServiceTokens =
        ServiceTokens(
            accessToken = Token(value = this.accessToken),
            tokenType = TokenType(value = this.type),
            expiration = this.expiration,
            expiresIn = this.expiresIn(),
            refreshToken = this.refreshToken?.let { Token(value = it) },
            scopeString = this.scope,
            issued = this.issued,
            userId = this.userId
        )

    private fun com.mooncloak.vpn.app.storage.sqlite.database.ServiceTokens.expiresIn(): Duration {
        val now = clock.now()

        return when {
            expiration != null -> expiration - now
            else -> 0.seconds
        }
    }
}
