package com.mooncloak.vpn.app.shared.api.server.usecase

import com.mooncloak.kodetools.konstruct.annotations.Inject
import com.mooncloak.vpn.api.shared.MooncloakVpnServiceHttpApi
import com.mooncloak.vpn.api.shared.key.Base64Key
import com.mooncloak.vpn.api.shared.server.RegisteredClient
import com.mooncloak.vpn.api.shared.server.RegisteredClientRepository
import com.mooncloak.vpn.api.shared.service.ServiceTokensRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

public class RegisterClientUseCase @Inject public constructor(
    private val registeredClientRepository: RegisteredClientRepository,
    private val mooncloakApi: MooncloakVpnServiceHttpApi,
    private val serviceTokensRepository: ServiceTokensRepository
) {

    public suspend operator fun invoke(
        serverId: String,
        publicKey: Base64Key
    ): RegisteredClient = withContext(Dispatchers.IO) {
        val token = serviceTokensRepository.getLatest()?.accessToken

        val client = mooncloakApi.registerClient(
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

        return@withContext client
    }
}
