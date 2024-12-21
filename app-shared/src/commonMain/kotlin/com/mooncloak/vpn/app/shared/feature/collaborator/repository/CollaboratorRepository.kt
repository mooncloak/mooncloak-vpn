package com.mooncloak.vpn.app.shared.feature.collaborator.repository

import com.mooncloak.vpn.app.shared.feature.collaborator.model.Collaborator
import kotlinx.coroutines.CancellationException
import kotlin.jvm.Throws

public interface CollaboratorRepository {

    @Throws(NoSuchElementException::class, CancellationException::class)
    public suspend fun get(id: String): Collaborator

    public suspend fun getAll(): List<Collaborator>

    public companion object
}

public suspend fun CollaboratorRepository.getOrNull(id: String): Collaborator? =
    try {
        get(id = id)
    } catch (_: NoSuchElementException) {
        null
    }
