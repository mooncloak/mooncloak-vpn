package com.mooncloak.vpn.app.shared.app

import com.mooncloak.vpn.app.info.BuildConfig

public sealed interface AppClientInfo {

    public val group: String
        get() = BuildConfig.group

    public val name: String
        get() = BuildConfig.appName

    public val description: String
        get() = BuildConfig.appDescription

    public val version: String
        get() = BuildConfig.appVersion

    public val versionCode: Int
        get() = BuildConfig.appVersionCode

    public val versionName: String
        get() = "$version-$versionCode"

    public val isDebug: Boolean
        get() = true // FIXME: Hardcoded debug value

    public companion object Mooncloak : AppClientInfo
}

public val AppClientInfo.Mooncloak.supportEmail: String get() = "support@mooncloak.com"

public val AppClientInfo.Mooncloak.supportFeatureRequestUri: String get() = "https://github.com/mooncloak/mooncloak-vpn/issues/new?assignees=&labels=&projects=&template=feature_request.md&title="

public val AppClientInfo.Mooncloak.supportIssueUri: String get() = "https://github.com/mooncloak/mooncloak-vpn/issues/new?assignees=&labels=&projects=&template=bug_report.md&title="

public val AppClientInfo.Mooncloak.rateAppUri: String get() = "https://play.google.com/"
