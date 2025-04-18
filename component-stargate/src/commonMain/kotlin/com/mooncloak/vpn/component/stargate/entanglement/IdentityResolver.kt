package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.util.shared.validation.fromOrNull
import kotlin.coroutines.cancellation.CancellationException

public interface IdentityResolver {

    @Throws(NoSuchElementException::class, IllegalArgumentException::class, CancellationException::class)
    public suspend fun resolve(did: DID): Identity

    @Throws(NoSuchElementException::class, IllegalArgumentException::class, CancellationException::class)
    public suspend fun resolve(handle: IdentityHandle): Identity

    @Throws(NoSuchElementException::class, IllegalArgumentException::class, CancellationException::class)
    public suspend fun resolve(value: String): Identity {
        val did = DID.fromOrNull(value)

        if (did != null) {
            return resolve(did = did)
        }

        val handle = IdentityHandle.fromOrNull(value)

        if (handle != null) {
            return resolve(handle = handle)
        }

        throw IllegalArgumentException("Invalid DID or Handle '$value'.")
    }

    public companion object
}

public suspend fun IdentityResolver.resolveOrNull(did: DID): Identity? =
    try {
        resolve(did = did)
    } catch (_: NoSuchElementException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }

public suspend fun IdentityResolver.resolveOrNull(handle: IdentityHandle): Identity? =
    try {
        resolve(handle = handle)
    } catch (_: NoSuchElementException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }

public suspend fun IdentityResolver.resolveOrNull(value: String): Identity? =
    try {
        resolve(value = value)
    } catch (_: NoSuchElementException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }
