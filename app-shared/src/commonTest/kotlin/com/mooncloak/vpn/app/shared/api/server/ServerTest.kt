package com.mooncloak.vpn.app.shared.api.server

import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.ServerStatus
import com.mooncloak.vpn.api.shared.server.isConnectable
import com.mooncloak.vpn.api.shared.server.isStatusConnectable
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ServerTest {

    @Test
    fun `isStatusConnectable returns true for null status`() {
        val server = Server(
            name = "123",
            status = null
        )

        assertTrue { server.isStatusConnectable() }
    }

    @Test
    fun `isStatusConnectable returns false when not active`() {
        val server = Server(
            name = "123",
            status = ServerStatus(
                active = false
            )
        )

        assertFalse { server.isStatusConnectable() }
    }

    @Test
    fun `isStatusConnectable returns false when not connectable`() {
        val server = Server(
            name = "123",
            status = ServerStatus(
                active = true,
                connectable = false
            )
        )

        assertFalse { server.isStatusConnectable() }
    }

    @Test
    fun `isStatusConnectable returns true when all conditions are met`() {
        val server = Server(
            name = "123",
            status = ServerStatus(
                active = true,
                connectable = true
            )
        )

        assertTrue { server.isStatusConnectable() }
    }

    @Test
    fun `isConnectable returns false when isStatusConnectable returns false`() {
        val server = Server(
            name = "123",
            ipV4Address = "1.1.1.1",
            ipV6Address = null,
            publicKey = "123",
            status = ServerStatus(
                active = false,
                connectable = false
            )
        )

        assertFalse { server.isConnectable(hasSubscription = true) }
    }

    @Test
    fun `isConnectable returns false when ip address is null`() {
        val server = Server(
            name = "123",
            ipV4Address = null,
            ipV6Address = null,
            publicKey = "123",
            status = null
        )

        assertFalse { server.isConnectable(hasSubscription = true) }
    }

    @Test
    fun `isConnectable returns false when public key is null`() {
        val server = Server(
            name = "123",
            ipV4Address = "1.1.1.1",
            ipV6Address = null,
            publicKey = null,
            status = null
        )

        assertFalse { server.isConnectable(hasSubscription = true) }
    }

    @Test
    fun `isConnectable returns true when requires subscription is false and other conditions are met`() {
        val server = Server(
            name = "123",
            ipV4Address = "1.1.1.1",
            ipV6Address = null,
            publicKey = "123",
            status = null,
            requiresSubscription = false
        )

        assertTrue { server.isConnectable(hasSubscription = false) }
    }

    @Test
    fun `isConnectable returns false when requires subscription is true and hasSubscription is false`() {
        val server = Server(
            name = "123",
            ipV4Address = "1.1.1.1",
            ipV6Address = null,
            publicKey = "123",
            status = null,
            requiresSubscription = true
        )

        assertFalse { server.isConnectable(hasSubscription = false) }
    }

    @Test
    fun `isConnectable returns true when requires subscription is true and hasSubscription is true`() {
        val server = Server(
            name = "123",
            ipV4Address = "1.1.1.1",
            ipV6Address = null,
            publicKey = "123",
            status = null,
            requiresSubscription = true
        )

        assertTrue { server.isConnectable(hasSubscription = true) }
    }
}
