package com.mooncloak.vpn.app.shared.feature.collaborator.shared.source

import com.mooncloak.kodetools.apix.core.ApiException
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.VpnServiceApi
import com.mooncloak.vpn.api.shared.app.Contributor
import com.mooncloak.vpn.app.shared.feature.collaborator.shared.repository.ContributorRepository

@OptIn(ExperimentalApixApi::class)
internal class ContributorApiSource @Inject internal constructor(
    private val api: VpnServiceApi
) : ContributorRepository {

    override suspend fun get(id: String): Contributor =
        try {
            api.getContributor(id = id)
        } catch (_: ApiException) {
            throw NoSuchElementException()
        }

    override suspend fun getAll(): List<Contributor> =
        api.getContributors().contributors
}
