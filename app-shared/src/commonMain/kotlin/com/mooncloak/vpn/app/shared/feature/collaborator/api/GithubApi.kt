package com.mooncloak.vpn.app.shared.feature.collaborator.api

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.feature.app.SharedBuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType

internal class GithubApi @Inject internal constructor(
    private val httpClient: HttpClient
) {

    internal suspend fun getCollaborators(
        owner: String,
        repository: String
    ): List<GithubCollaborator> {
        val httpResponse = httpClient.get("https://api.github.com/repos/$owner/$repository/collaborators") {
            accept(ContentType.Application.Json)
            bearerAuth(getGithubApiToken())
        }

        return httpResponse.body()
    }

    internal suspend fun getCommits(
        owner: String,
        repository: String,
        author: String? = null,
        committer: String? = null,
        count: Int? = null,
        page: Int? = null
    ): List<GithubCommit> {
        val httpResponse = httpClient.get("https://api.github.com/repos/$owner/$repository/commits") {
            accept(ContentType.Application.Json)
            bearerAuth(getGithubApiToken())

            if (author != null) {
                parameter(key = "author", value = author)
            }

            if (committer != null) {
                parameter(key = "committer", value = committer)
            }

            if (count != null) {
                parameter(key = "per_page", value = count)
            }

            if (page != null) {
                parameter(key = "page", value = page)
            }
        }

        return httpResponse.body()
    }

    private fun getGithubApiToken(): String = SharedBuildConfig.githubApiToken!!
}
