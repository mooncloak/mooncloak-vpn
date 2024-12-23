package com.mooncloak.vpn.app.shared.feature.collaborator.source

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.app.Contributor
import com.mooncloak.vpn.app.shared.feature.collaborator.repository.ContributorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalApixApi::class)
internal class ContributorApiSource @Inject internal constructor(
    private val api: MooncloakVpnServiceHttpApi
) : ContributorRepository {

    override suspend fun get(id: String): Contributor =
        withContext(Dispatchers.IO) {
            try {
                api.getContributor(id = id)
            } catch (_: ApiException) {
                throw NoSuchElementException()
            }
        }

    override suspend fun getAll(): List<Contributor> =
        withContext(Dispatchers.IO) {
            api.getContributors().contributors
        }
}
