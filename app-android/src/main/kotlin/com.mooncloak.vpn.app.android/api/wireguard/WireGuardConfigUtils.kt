package com.mooncloak.vpn.app.android.api.wireguard

import com.mooncloak.vpn.app.shared.api.server.Server
import com.wireguard.config.Config
import com.wireguard.config.Interface
import com.wireguard.config.Peer
import com.wireguard.crypto.Key
import com.wireguard.crypto.KeyPair

internal fun Server.wireguardConfig(keyPair: KeyPair): Config =
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
                        this.publicKey ?: error("Servers public key was `null`. Cannot connect to server.")
                    )
                )
                .build()
        )
        .build()
