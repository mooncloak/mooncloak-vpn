package com.mooncloak.vpn.app.shared.feature.collaborator.source

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.feature.collaborator.api.GithubApi
import com.mooncloak.vpn.app.shared.feature.collaborator.model.Collaborator
import com.mooncloak.vpn.app.shared.feature.collaborator.repository.CollaboratorRepository

internal class GithubCollaboratorSource @Inject internal constructor(
    private val githubApi: GithubApi
) : CollaboratorRepository {

    private val cachedMap = mutableMapOf<String, Collaborator>()

    override suspend fun get(id: String): Collaborator {
        if (cachedMap.isEmpty()) {
            getAll()
        }

        return cachedMap[id] ?: throw NoSuchElementException("No collaborator found with id '$id'.")
    }

    override suspend fun getAll(): List<Collaborator> {
        val collaborators = githubApi.getCollaborators(
            owner = "mooncloak",
            repository = "mooncloak-vpn"
        ).map { collaborator ->
            Collaborator(
                id = collaborator.id.toString(),
                name = collaborator.login,
                contact = null,
                avatarUri = collaborator.avatarUrl,
                url = collaborator.url,
                commitCount = null
            )
        }

        collaborators.forEach { collaborator -> cachedMap[collaborator.id] = collaborator }

        return collaborators
    }
}
