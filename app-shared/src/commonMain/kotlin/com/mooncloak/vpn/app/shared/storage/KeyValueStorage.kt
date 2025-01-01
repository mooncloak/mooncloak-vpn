package com.mooncloak.vpn.app.shared.storage

import com.mooncloak.kodetools.konstruct.annotations.Inject

public class KeyValueStorage @Inject public constructor(
    public val preferences: PreferencesStorage,
    public val app: AppStorage,
    public val subscription: SubscriptionStorage
)
