package com.mooncloak.vpn.app.shared.api.key

public interface KeyManager<KeyMaterial> {

    public suspend fun generate(): KeyMaterial

    public suspend fun store(material: KeyMaterial)

    public suspend fun get(): KeyMaterial?

    public suspend fun generateAndStore(): KeyMaterial {
        val material = generate()

        store(material)

        return material
    }

    public companion object
}
