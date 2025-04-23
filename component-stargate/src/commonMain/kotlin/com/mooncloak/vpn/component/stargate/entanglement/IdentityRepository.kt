package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.data.shared.repository.MutableRepository
import kotlin.coroutines.cancellation.CancellationException

public interface IdentityRepository : MutableRepository<Identity> {

    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun getByDid(did: DID): Identity =
        get(id = did.value)

    @Throws(IllegalArgumentException::class, CancellationException::class)
    public suspend fun getByHandle(handle: IdentityHandle): Identity

    public companion object
}

public suspend fun IdentityRepository.getByDidOrNull(did: DID): Identity? =
    try {
        getByDid(did = did)
    } catch (_: NoSuchElementException) {
        null
    }

public suspend fun IdentityRepository.getByHandleOrNull(handle: IdentityHandle): Identity? =
    try {
        getByHandle(handle = handle)
    } catch (_: NoSuchElementException) {
        null
    }
