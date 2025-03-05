package com.mooncloak.vpn.util.shortcuts

/**
 * A manager component to handle the coordination of [AppShortcut]s. Application shortcuts are quick actions that can
 * be performed without explicitly opening the application.
 *
 * > [!Note]
 * > [AppShortcut]s are platform-specific and not all platforms will handle them. For instance, on Android these
 * > translate to "dynamic app shortcuts" (via the ShortcutInfo class), whereas, on iOS these translate to the
 * > UIApplicationShortcutItem class.
 *
 * > [!Note]
 * > [AppShortcut]s might not be directly supported on the JVM, as there is no known JVM abstraction for them (even if
 * > some desktop operating systems support them). However, an implementation of this component might choose to display
 * > [AppShortcut]s within the status bar menu or something similar.
 *
 * @see [AppShortcut]
 */
public interface AppShortcutManager {

    /**
     * Retrieves the [AppShortcut] whose [AppShortcut.id] matches the provided [id] value, or `null` if there is no
     * [AppShortcut] with the provided [id].
     *
     * > [!Warning]
     * > The implementation is not required to retain [AppShortcut]s permanently. Therefore, this function may return
     * > `null` even though an [AppShortcut] with that id is currently set via the platform API.
     *
     * @param [id] The [AppShortcut.id] value of the [AppShortcut] to retrieve.
     *
     * @return The [AppShortcut] with the provided [id] or `null`.
     */
    public suspend fun get(id: String): AppShortcut?

    /**
     * Sets the [AppShortcut]s for this application.
     *
     * @param [shortcuts] The [Collection] of [AppShortcut]s to set for this application.
     */
    public suspend fun set(shortcuts: Collection<AppShortcut>)

    /**
     * Removes the [AppShortcut] with the provided [id] if it is available in the current [AppShortcut]s.
     */
    public suspend fun remove(id: String)

    /**
     * Clears all [AppShortcut]s for this application.
     */
    public suspend fun clear()

    public companion object
}
