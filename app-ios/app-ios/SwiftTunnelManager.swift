//
//  SwiftTunnelManager.swift
//  app-ios
//
//  Created by Chris Keenan on 4/10/25.
//

import Foundation
import NetworkExtension
import WireGuardKit
import app_shared

@objc public class SwiftTunnelManager: IosWireGuardTunnelManager {
    
    private var tunnelManagers: [NETunnelProviderManager] = []
    private var connectionKeyPairResolver: WireGuardConnectionKeyPairResolver
    private var preferenceProvider: Data_sharedProvider

        
    init(
        coroutineScope: Kotlinx_coroutines_coreCoroutineScope,
        connectionKeyPairResolver: WireGuardConnectionKeyPairResolver,
        preferenceProvider: Data_sharedProvider
    ) {
        self.connectionKeyPairResolver = connectionKeyPairResolver
        self.preferenceProvider = preferenceProvider
        super.init(coroutineScope: coroutineScope)
    }
    
    override public func sync() async throws {
        let managers = try await NETunnelProviderManager.loadAllFromPreferences()
        tunnelManagers = managers.filter { $0.protocolConfiguration is NETunnelProviderProtocol }
        
        let tunnels = tunnelManagers.map { manager in
            let tunnel = mapToTunnel(manager: manager)
            
            let status: Api_vpnVPNConnectionStatus
            switch manager.connection.status {
            case .connected: status = .connected
            case .disconnected: status = .disconnected
            case .connecting: status = .connecting
            case .disconnecting: status = .disconnecting
            default: status = .disconnected
            }
            
            tunnel.updateStatus(status: status)
            
            return tunnel
        }
        
        updateTunnels(tunnels: tunnels)
    }
    
    override public func connect(server: Api_vpnServer) async throws -> Network_coreIosTunnel? {
        let config = try await generateWireGuardConfig(from: server)
                
        let managers = try await NETunnelProviderManager.loadAllFromPreferences()
        var manager = managers.first { $0.localizedDescription == server.name as String } ?? NETunnelProviderManager()
        
        manager.localizedDescription = server.name as String
        let providerProtocol = NETunnelProviderProtocol()
        providerProtocol.providerBundleIdentifier = "com.mooncloak.vpn.app-ios.WireGuardExtension"
        providerProtocol.serverAddress = server.ipV4Address ?? server.ipV6Address ?? server.hostname
        providerProtocol.providerConfiguration = [
            "sessionId": UUID().uuidString,
            "wgConfig": config
        ]
        
        manager.protocolConfiguration = providerProtocol
        manager.isEnabled = true
        
        try await manager.saveToPreferences()
        try manager.connection.startVPNTunnel()
        
        let tunnel = Network_coreIosTunnel(
            tunnelName: server.name,
            sessionId: ((providerProtocol.providerConfiguration?["sessionId"] as? NSString) ?? "" as NSString) as String,
            server: server
        )
        
        tunnel.updateStatus(status: Api_vpnVPNConnectionStatus.connecting)
        
        tunnelManagers.append(manager)
        
        let updatedTunnels = tunnelManagers.map { mgr in
            let t = mapToTunnel(manager: mgr)
            
            t.updateStatus(status: mgr.connection.status == .connected ? .connected : .disconnected)
            
            return t
        } + [tunnel]
        
        updateTunnels(tunnels: updatedTunnels)
        
        return tunnel
    }
    
    override public func disconnect(tunnelName: String) async throws {
        guard let manager = tunnelManagers.first(where: { $0.localizedDescription == tunnelName }) else {
            throw TunnelError.tunnelNotFound
        }
        
        manager.connection.stopVPNTunnel()
        try await manager.removeFromPreferences()
        
        tunnelManagers.removeAll { $0.localizedDescription == tunnelName }
        let updatedTunnels = tunnelManagers.map { mgr in
            let tunnel = mapToTunnel(manager: mgr)
            
            tunnel.updateStatus(status: .disconnected)
            
            return tunnel
        }
        
        updateTunnels(tunnels: updatedTunnels)
    }
    
    override public func disconnectAll() async throws {
        for manager in tunnelManagers {
            manager.connection.stopVPNTunnel()
            
            try await manager.removeFromPreferences()
        }
        
        tunnelManagers.removeAll()
        
        updateTunnels(tunnels: [])
    }
    
    private func mapToTunnel(manager: NETunnelProviderManager, server: Api_vpnServer? = nil) -> Network_coreIosTunnel {
        let tunnelName = manager.localizedDescription ?? "Unnamed Tunnel"
        let sessionId = (manager.protocolConfiguration as? NETunnelProviderProtocol)?.providerConfiguration?["sessionId"] as? String ?? ""
        let tunnel = Network_coreIosTunnel(tunnelName: tunnelName, sessionId: sessionId, server: server)
        
        let status: Api_vpnVPNConnectionStatus = manager.connection.status == .connected ? .connected : .disconnected
        
        tunnel.updateStatus(status: status)
        
        return tunnel
    }
    
    private func generateWireGuardConfig(from server: Api_vpnServer) async throws -> String {
        guard let serverPublicKey = server.publicKey as String?,
              let serverIP = server.ipV4Address ?? server.ipV6Address,
              let serverPort = server.port else {
            throw TunnelError.configurationFailed
        }
        
        // TODO: Support moonshield, assigned address, etc. for WireGuard config.
        let preferences = try await preferenceProvider.get() as! Api_vpnWireGuardPreferences
        let dns = preferences.dnsAddresses.joined(separator: ", ")
        
        // Call the Kotlin suspend function resolve() from Swift
        let keyPair = try await withCheckedThrowingContinuation { (continuation: CheckedContinuation<Api_vpnWireGuardConnectionKeyPair, Error>) in
            connectionKeyPairResolver.resolve { result, error in
                if let error = error {
                    continuation.resume(throwing: error)
                } else if let result = result {
                    continuation.resume(returning: result)
                } else {
                    continuation.resume(throwing: TunnelError.configurationFailed)
                }
            }
        }
        
        let privateKey = keyPair.privateKey as! String
        let publicKey = keyPair.publicKey as! String
        
        let config = """
        [Interface]
        PrivateKey = \(privateKey)
        Address = 10.0.0.2/32
        DNS = \(dns)
        
        [Peer]
        PublicKey = \(serverPublicKey)
        AllowedIPs = 0.0.0.0/0, ::/0
        Endpoint = \(serverIP):\(serverPort)
        PersistentKeepalive = 25
        """
        
        return config
    }
}

@objc public class SwiftTunnelManagerFactory : NSObject, IosWireGuardTunnelManagerFactory {
    
    public func create(
        coroutineScope: Kotlinx_coroutines_coreCoroutineScope,
        connectionKeyPairResolver: WireGuardConnectionKeyPairResolver,
        preferenceProvider: Data_sharedProvider
    ) -> IosWireGuardTunnelManager {
        SwiftTunnelManager(
            coroutineScope: coroutineScope,
            connectionKeyPairResolver: connectionKeyPairResolver,
            preferenceProvider: preferenceProvider
        )
    }
}

enum TunnelError: Error {
    case tunnelNotFound
    case configurationFailed
}
