package com.mooncloak.vpn.app.shared.feature.collaborator.repository

import com.mooncloak.vpn.app.shared.api.app.Contributor
import kotlinx.coroutines.CancellationException
import kotlin.jvm.Throws

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
