package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.vpn.api.shared.preference.WireGuardPreferences
import com.mooncloak.vpn.api.shared.server.RegisteredClient
import com.mooncloak.vpn.api.shared.server.Server
import com.mooncloak.vpn.api.shared.server.requireIpAddress
import com.mooncloak.vpn.api.shared.server.requireWireGuardEndpoint
import com.wireguard.config.Config
import com.wireguard.config.InetAddresses
import com.wireguard.config.InetEndpoint
import com.wireguard.config.InetNetwork
import com.wireguard.config.Interface
import com.wireguard.config.Peer
import com.wireguard.crypto.Key
import com.wireguard.crypto.KeyPair

internal fun Server.toWireGuardConfig(
    keyPair: KeyPair,
    client: RegisteredClient,
    wireGuardPreferences: WireGuardPreferences,
    moonShieldEnabled: Boolean = false
): Config {
    val interfaceBuilder = Interface.Builder()
        .setKeyPair(keyPair)
        .addAddress(InetNetwork.parse(client.assignedAddress))

    // When MoonShield is enabled, we set the VPN server's IP Address as the first DNS server entry. This is because
    // the VPN server is also set up to be a filtering DNS server for subscribers.
    if (moonShieldEnabled) {
        interfaceBuilder.addDnsServer(InetAddresses.parse(requireIpAddress()))
    }

    wireGuardPreferences.dnsAddresses.forEach { dnsAddress ->
        interfaceBuilder.addDnsServer(InetAddresses.parse(dnsAddress))
    }

    var peerBuilder = Peer.Builder()
        .setPublicKey(
            Key.fromBase64(
                this.publicKey?.trim()
                    ?: error("No Server public key found for server with id '${this.id}'. Cannot connect to server.")
            )
        )
        .setEndpoint(
            InetEndpoint.parse(requireWireGuardEndpoint())
        )

    wireGuardPreferences.allowedIps.forEach { ip ->
        peerBuilder = peerBuilder.addAllowedIp(InetNetwork.parse(ip))
    }

    return Config.Builder()
        .setInterface(interfaceBuilder.build())
        .addPeer(peerBuilder.build())
        .build()
}
