package com.mooncloak.vpn.app.shared.feature.collaborator.shared.source

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.app.Contributor
import com.mooncloak.vpn.app.shared.di.FeatureScoped
import com.mooncloak.vpn.app.shared.feature.collaborator.shared.repository.ContributorRepository
import com.mooncloak.vpn.app.shared.feature.collaborator.shared.repository.getOrNull

@FeatureScoped
internal class ContributorSource @Inject internal constructor(
    private val cacheSource: ContributorInMemoryCacheSource,
    private val apiSource: ContributorApiSource
) : ContributorRepository {

    override suspend fun get(id: String): Contributor {
        cacheSource.getOrNull(id)?.let { return it }

        val contributor = apiSource.get(id = id)

        cacheSource.add(contributor)

        return contributor
    }

    override suspend fun getAll(): List<Contributor> {
        var contributors = cacheSource.getAll()

        if (contributors.isNotEmpty()) {
            return contributors
        }

        contributors = apiSource.getAll()

        cacheSource.addAll(contributors)

        return contributors
    }
}
