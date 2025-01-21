package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.vpn.app.shared.api.server.RegisteredClient
import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.requireWireGuardEndpoint
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
    client: RegisteredClient
): Config {
    val interfaceBuilder = Interface.Builder()
        .setKeyPair(keyPair)
        .addAddress(InetNetwork.parse(client.assignedAddress))
        .addDnsServer(InetAddresses.parse("1.1.1.1"))
        .addDnsServer(InetAddresses.parse("8.8.8.8"))

    LogPile.info(message = "wireguard: interface: $interfaceBuilder: address: ${InetNetwork.parse(client.assignedAddress)}")

    return Config.Builder()
        .setInterface(interfaceBuilder.build())
        .addPeer(
            Peer.Builder()
                .setPublicKey(
                    Key.fromBase64(
                        this.publicKey
                            ?: error("No Server public key found for server with id '${this.id}'. Cannot connect to server.")
                    )
                )
                .setEndpoint(
                    InetEndpoint.parse(requireWireGuardEndpoint())
                )
                .addAllowedIp(InetNetwork.parse("0.0.0.0/0"))
                .build()
        )
        .build()
        .apply {
            LogPile.info(message = "wireguard: config: $this")
        }
}
