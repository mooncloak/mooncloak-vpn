//
//  IosWireGuardConnectionKeyManager.swift
//  app-ios
//
//  Created by Chris Keenan on 4/10/25.
//

import Foundation
import app_shared
import os
import WireGuardKit

@objc public class IosWireGuardConnectionKeyManager : NSObject, Api_vpnWireGuardConnectionKeyManager {
    
    private static let service = "com.mooncloak.vpn.app.ios.key.wireguard"
    private static let privateKeyTag = "wireguard_private_key"
    
    public func generate() async throws -> Any? {
        let privateKey = PrivateKey()
        let publicKey = privateKey.publicKey
        
        return IosWireGuardConnectionKeyPair(
            publicKey: publicKey.base64Key,
            privateKey_: privateKey.base64Key
        )
    }
    
    public func get() async throws -> Any? {
        let query: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: Self.service,
            kSecAttrAccount as String: Self.privateKeyTag,
            kSecReturnData as String: kCFBooleanTrue as Any,
            kSecMatchLimit as String: kSecMatchLimitOne
        ]
        
        var item: CFTypeRef?
        let status = SecItemCopyMatching(query as CFDictionary, &item)
        
        switch status {
        case errSecSuccess:
            guard let data = item as? Data,
                  let privateKeyBase64 = String(data: data, encoding: .utf8) else {
                throw NSError(domain: "KeychainError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to decode private key"])
            }
            
            guard let privateKeyData = Data(base64Encoded: privateKeyBase64),
                  let privateKey = PrivateKey(rawValue: privateKeyData) else {
                return nil // Key found but invalid
            }
            
            return IosWireGuardConnectionKeyPair(
                publicKey: privateKey.publicKey.base64Key,
                privateKey_: privateKey.base64Key
            )
        case errSecItemNotFound:
            return nil // No key found
            
        default:
            throw NSError(domain: "KeychainError", code: Int(status), userInfo: [NSLocalizedDescriptionKey: "Failed to retrieve key: \(status)"])
        }
    }
    
    public func store(material: Any?) async throws {
        let materialKeyPair = material as! IosWireGuardConnectionKeyPair
        
        // Delete existing key
        let deleteQuery: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: Self.service,
            kSecAttrAccount as String: Self.privateKeyTag
        ]
        
        SecItemDelete(deleteQuery as CFDictionary)
        
        // Store new key
        guard let privateKeyData = materialKeyPair.privateKeyBase64.data(using: .utf8) else {
            throw NSError(domain: "KeychainError", code: -1, userInfo: [NSLocalizedDescriptionKey: "Failed to encode private key"])
        }
        
        let addQuery: [String: Any] = [
            kSecClass as String: kSecClassGenericPassword,
            kSecAttrService as String: Self.service,
            kSecAttrAccount as String: Self.privateKeyTag,
            kSecValueData as String: privateKeyData,
            kSecAttrAccessible as String: kSecAttrAccessibleWhenUnlocked
        ]
        
        let status = SecItemAdd(addQuery as CFDictionary, nil)
        
        guard status == errSecSuccess else {
            throw NSError(domain: "KeychainError", code: Int(status), userInfo: [NSLocalizedDescriptionKey: "Failed to store key: \(status)"])
        }
    }
}
