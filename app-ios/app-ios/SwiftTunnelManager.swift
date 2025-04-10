//
//  SwiftTunnelManager.swift
//  app-ios
//
//  Created by Chris Keenan on 4/10/25.
//

import Foundation
import app_shared

@objc public class SwiftTunnelManager: IosWireGuardTunnelManager {
    
    override public func sync() async throws {
        fatalError("Not Implemented")
    }
    
    override public func connect(server: Api_vpnServer) async throws -> Network_coreTunnel? {
        fatalError("Not Implemented")
    }
    
    override public func disconnect(tunnelName: String) async throws {
        fatalError("Not Implemented")
    }
    
    override public func disconnectAll() async throws {
        fatalError("Not Implemented")
    }
}

@objc public class SwiftTunnelManagerFactory : NSObject, IosWireGuardTunnelManagerFactory {
    
    public func create(coroutineScope: Kotlinx_coroutines_coreCoroutineScope) -> IosWireGuardTunnelManager {
        SwiftTunnelManager(coroutineScope: coroutineScope)
    }
}
