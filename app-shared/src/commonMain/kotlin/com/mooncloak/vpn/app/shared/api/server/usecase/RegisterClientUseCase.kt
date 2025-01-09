package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.kodetools.statex.persistence.ExperimentalPersistentStateAPI
import com.mooncloak.vpn.app.shared.api.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.app.shared.api.key.Base64Key
import com.mooncloak.vpn.app.shared.api.server.RegisteredClient
import com.mooncloak.vpn.app.shared.api.server.RegisteredClientRepository
import com.mooncloak.vpn.app.shared.api.server.getByServerIdOrNull
import com.mooncloak.vpn.app.shared.storage.SubscriptionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class RegisterClientUseCase @Inject public constructor(
    private val registeredClientRepository: RegisteredClientRepository,
    private val mooncloakApi: MooncloakVpnServiceHttpApi,
    private val subscriptionStorage: SubscriptionStorage
) {

    @OptIn(ExperimentalPersistentStateAPI::class)
    public suspend operator fun invoke(
        serverId: String,
        publicKey: Base64Key
    ): RegisteredClient = withContext(Dispatchers.IO) {
        var client = registeredClientRepository.getByServerIdOrNull(serverId = serverId)

        if (client == null || client.publicKey != publicKey) {
            val token = subscriptionStorage.tokens.current.value?.accessToken

            client = mooncloakApi.registerClient(
                serverId = serverId,
                clientPublicKey = publicKey,
                token = token
            )

            registeredClientRepository.insert(
                tokenId = client.tokenId,
                protocol = client.protocol,
                clientId = client.clientId,
                registered = client.registered,
                expiration = client.expiration,
                publicKey = client.publicKey,
                publicKeyId = client.publicKeyId,
                allowedIpAddresses = client.allowedIpAddresses,
                persistentKeepAlive = client.persistentKeepAlive,
                endpoint = client.endpoint,
                serverId = client.serverId ?: serverId,
                assignedAddress = client.assignedAddress
            )
        }

        return@withContext client
    }
}
