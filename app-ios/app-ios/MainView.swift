//
//  MainView.swift
//  app-ios
//
//  Created by Chris Keenan on 3/18/25.
//

import Foundation
import app_shared
import SwiftUI

struct MainView: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController(
            cryptoWalletManagerFactory: SwiftCryptoWalletManagerFactory(),
            tunnelManagerFactory: SwiftTunnelManagerFactory(),
            billingManagerFactory: StoreKit2BillingManagerFactory(),
            connectionKeyManager: IosWireGuardConnectionKeyManager()
        )
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}
