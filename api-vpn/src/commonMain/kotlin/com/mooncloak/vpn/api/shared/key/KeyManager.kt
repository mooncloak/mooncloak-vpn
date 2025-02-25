package com.mooncloak.vpn.api.shared.key

public interface KeyManager<KeyMaterial> {

    public suspend fun generate(): KeyMaterial

    public suspend fun store(material: KeyMaterial)

    public suspend fun get(): KeyMaterial?

    public companion object
}
