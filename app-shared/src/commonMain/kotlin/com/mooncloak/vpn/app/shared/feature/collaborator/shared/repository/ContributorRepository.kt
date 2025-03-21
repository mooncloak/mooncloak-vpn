package com.mooncloak.vpn.app.shared.feature.collaborator.shared.repository

import com.mooncloak.vpn.api.shared.app.Contributor
import kotlin.coroutines.cancellation.CancellationException

public interface ContributorRepository {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): Contributor

    public suspend fun getAll(): List<Contributor>

    public companion object
}

public suspend fun ContributorRepository.getOrNull(id: String): Contributor? =
    try {
        get(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
