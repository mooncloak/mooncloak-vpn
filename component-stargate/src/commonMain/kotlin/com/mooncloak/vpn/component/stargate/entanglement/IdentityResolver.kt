package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.util.shared.validation.fromOrNull
import kotlinx.datetime.Clock
import kotlin.coroutines.cancellation.CancellationException

public interface IdentityResolver {

    @Throws(
        NoSuchElementException::class,
        IllegalArgumentException::class,
        IllegalStateException::class,
        CancellationException::class
    )
    public suspend fun resolve(did: DID): Identity

    @Throws(
        NoSuchElementException::class,
        IllegalArgumentException::class,
        IllegalStateException::class,
        CancellationException::class
    )
    public suspend fun resolve(handle: IdentityHandle): Identity

    @Throws(
        NoSuchElementException::class,
        IllegalArgumentException::class,
        IllegalStateException::class,
        CancellationException::class
    )
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
    } catch (_: IllegalStateException) {
        null
    }

public suspend fun IdentityResolver.resolveOrNull(handle: IdentityHandle): Identity? =
    try {
        resolve(handle = handle)
    } catch (_: NoSuchElementException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    } catch (_: IllegalStateException) {
        null
    }

public suspend fun IdentityResolver.resolveOrNull(value: String): Identity? =
    try {
        resolve(value = value)
    } catch (_: NoSuchElementException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    } catch (_: IllegalStateException) {
        null
    }

internal class DefaultIdentityResolver internal constructor(
    private val identityRepository: IdentityRepository,
    private val didResolver: DIDResolver,
    private val didDocumentResolver: DIDDocumentResolver,
    private val didDocumentIdentityHandleProvider: DIDDocumentIdentityHandleProvider,
    private val profileResolver: ProfileResolver,
    private val clock: Clock
) : IdentityResolver {

    override suspend fun resolve(did: DID): Identity {
        val identity = identityRepository.getByDidOrNull(did = did)

        if (identity != null && identity.isValid(at = clock.now())) {
            return identity
        }

        val document = didDocumentResolver.resolve(did = did)

        return getIdentity(
            handle = identity?.handle,
            contact = identity?.contact,
            did = did,
            document = document
        )
    }

    override suspend fun resolve(handle: IdentityHandle): Identity {
        val did = didResolver.resolve(handle = handle)

        val identity = identityRepository.getByDidOrNull(did = did)

        if (identity != null && identity.handle != handle) {
            throw IllegalStateException("Provided Identity Handle value '$handle' did not match the handle in the stored value '${identity.handle}'.")
        }

        if (identity != null && identity.isValid(at = clock.now())) {
            return identity
        }

        val document = didDocumentResolver.resolve(did = did)

        return getIdentity(
            handle = handle,
            contact = identity?.contact,
            did = did,
            document = document
        )
    }

    private suspend fun getIdentity(
        handle: IdentityHandle?,
        contact: Contact?,
        did: DID,
        document: DIDDocument
    ): Identity {
        if (handle != null) {
            val allHandles = didDocumentIdentityHandleProvider.get(document)

            if (!allHandles.contains(handle)) {
                throw IllegalStateException("Identity Handle used to obtain DID, is not present in the associated DID Document. This means the Identity Handle does not currently belong to the DID.")
            }
        }

        val profile = profileResolver.resolve(document)
        val now = clock.now()

        val identity = Identity(
            did = did,
            document = document,
            handle = handle,
            profile = profile,
            contact = contact,
            resolved = now
        )

        identityRepository.upsert(
            id = identity.did.value,
            insert = { identity },
            update = { identity }
        )

        return identity
    }
}
