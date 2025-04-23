package com.mooncloak.vpn.component.stargate.entanglement

import com.mooncloak.vpn.util.shared.validation.fromOrNull

public interface DIDDocumentIdentityHandleProvider {

    public fun get(document: DIDDocument): List<IdentityHandle>

    public companion object
}

internal object AtProtocolDidDocumentIdentityHandleProvider : DIDDocumentIdentityHandleProvider {

    override fun get(document: DIDDocument): List<IdentityHandle> =
        document.alsoKnownAs.filter { it.startsWith("at://") }
            .map { it.removePrefix("at://") }
            .mapNotNull { IdentityHandle.fromOrNull(it) }
}
