package com.mooncloak.vpn.app.shared.info

import com.mooncloak.vpn.feature.app.SharedBuildConfig
import kotlinx.datetime.Instant

public interface AppClientInfo {

    public val group: String
        get() = SharedBuildConfig.group

    public val name: String
        get() = SharedBuildConfig.appName

    public val description: String
        get() = SharedBuildConfig.appDescription

    public val version: String
        get() = SharedBuildConfig.appVersion

    public val versionCode: Int
        get() = SharedBuildConfig.appVersionCode

    public val versionName: String
        get() = "$version-$versionCode"

    public val buildTime: Instant
        get() = Instant.parse(SharedBuildConfig.appBuildTime)

    public val flavor: String?

    public val buildType: String?

    public val isDebug: Boolean

    public val isGooglePlayBuild: Boolean

    public val supportEmail: String get() = "support@mooncloak.com"

    public val supportFeatureRequestUri: String get() = "https://github.com/mooncloak/mooncloak-vpn/issues/new?assignees=&labels=&projects=&template=feature_request.md&title="

    public val supportIssueUri: String get() = "https://github.com/mooncloak/mooncloak-vpn/issues/new?assignees=&labels=&projects=&template=bug_report.md&title="

    public val rateAppUri: String get() = "https://play.google.com/"

    public val privacyPolicyUri: String get() = "https://mooncloak.com/vpn/legal/privacy"

    public val termsAndConditionsUri: String get() = "https://mooncloak.com/vpn/legal/terms"

    public val sourceCodeUri: String get() = "https://github.com/mooncloak/mooncloak-vpn"
}
