package com.mooncloak.vpn.app.shared.feature.collaborator.shared.source

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.app.Contributor
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.collaborator.shared.repository.ContributorRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

// FIXME: The scope is being ignored and multiple instances of this class are being created. This is causing the cache
//  to now work. It might have to do with the way the component is constructed and teared down with the composable
//  lifecycle.
@FeatureScoped
internal class ContributorInMemoryCacheSource @Inject internal constructor(
    initialValues: List<Contributor> = emptyList(),
    private val clock: Clock
) : ContributorRepository {

    private val contributorMap = mutableMapOf<String, Contributor>().apply {
        putAll(initialValues.associateBy { it.id })
    }

    private val mutex = Mutex(locked = false)

    // Arbitrarily chosen "timeout" duration. I don't think these values need to be updated often, so I chose a rather
    // large time period.
    private val timeout: Duration = 5.minutes

    private var lastUpdated: Instant = clock.now()

    override suspend fun get(id: String): Contributor {
        if (!isCacheValid()) {
            contributorMap.remove(id)

            throw NoSuchElementException("No Contributor found with id '$id'.")
        }

        return contributorMap[id] ?: throw NoSuchElementException("No Contributor found with id '$id'.")
    }

    override suspend fun getAll(): List<Contributor> {
        if (!isCacheValid()) {
            contributorMap.clear()

            return emptyList()
        }

        return contributorMap.values.toList()
    }

    internal suspend fun add(contributor: Contributor) {
        mutate {
            contributorMap[contributor.id] = contributor
        }
    }

    internal suspend fun addAll(contributors: Collection<Contributor>) {
        mutate {
            contributorMap.putAll(contributors.associateBy { it.id })
        }
    }

    internal suspend fun remove(id: String): Contributor? =
        mutate {
            contributorMap.remove(id)
        }

    internal suspend fun clear() {
        mutate {
            contributorMap.clear()
        }
    }

    private suspend fun <R> mutate(block: suspend () -> R): R = mutex.withLock {
        val result = block.invoke()

        lastUpdated = clock.now()

        return@withLock result
    }

    private fun isCacheValid(): Boolean {
        val now = clock.now()

        return now < (lastUpdated + timeout)
    }
}
