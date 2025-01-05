package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.vpn.app.shared.api.server.Server
import com.mooncloak.vpn.app.shared.api.server.requireIpAddress
import com.wireguard.config.Config
import com.wireguard.config.InetEndpoint
import com.wireguard.config.Interface
import com.wireguard.config.Peer
import com.wireguard.crypto.Key
import com.wireguard.crypto.KeyPair

internal fun Server.toWireGuardConfig(keyPair: KeyPair): Config =
    Config.Builder()
        .setInterface(
            Interface.Builder()
                .setKeyPair(keyPair)
                .build()
        )
        .addPeer(
            Peer.Builder()
                .setPublicKey(
                    Key.fromBase64(
                        this.publicKey
                            ?: error("No Server public key found for server with id '${this.id}'. Cannot connect to server.")
                    )
                )
                .setEndpoint(
                    InetEndpoint.parse(buildString {
                        append(requireIpAddress())

                        if (port != null) {
                            append(":$port")
                        }
                    })
                )
                .build()
        )
        .build()
