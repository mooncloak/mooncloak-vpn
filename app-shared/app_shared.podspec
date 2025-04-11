Pod::Spec.new do |spec|
    spec.name                     = 'app_shared'
    spec.version                  = '1.0.0-beta10'
    spec.homepage                 = 'https://mooncloak.com'
    spec.source                   = { :http=> ''}
    spec.authors                  = 'mooncloak Team'
    spec.license                  = 'https://github.com/mooncloak/mooncloak-vpn/blob/main/LICENSE'
    spec.summary                  = 'Shared app module for the mooncloak VPN application.'
    spec.vendored_frameworks      = 'build/cocoapods/framework/app_shared.framework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target    = '17.0'
                
                
    if !Dir.exist?('build/cocoapods/framework/app_shared.framework') || Dir.empty?('build/cocoapods/framework/app_shared.framework')
        raise "

        Kotlin framework 'app_shared' doesn't exist yet, so a proper Xcode project can't be generated.
        'pod install' should be executed after running ':generateDummyFramework' Gradle task:

            ./gradlew :app-shared:generateDummyFramework

        Alternatively, proper pod installation is performed during Gradle sync in the IDE (if Podfile location is set)"
    end
                
    spec.xcconfig = {
        'ENABLE_USER_SCRIPT_SANDBOXING' => 'NO',
    }
                
    spec.pod_target_xcconfig = {
        'KOTLIN_PROJECT_PATH' => ':app-shared',
        'PRODUCT_MODULE_NAME' => 'app_shared',
    }
                
    spec.script_phases = [
        {
            :name => 'Build app_shared',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
                  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
                  exit 0
                fi
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/../gradlew" -p "$REPO_ROOT" $KOTLIN_PROJECT_PATH:syncFramework \
                    -Pkotlin.native.cocoapods.platform=$PLATFORM_NAME \
                    -Pkotlin.native.cocoapods.archs="$ARCHS" \
                    -Pkotlin.native.cocoapods.configuration="$CONFIGURATION"
            SCRIPT
        }
    ]
    spec.resources = ['build/compose/cocoapods/compose-resources']
    spec.info_plist = {
    "CFBundleShortVersionString": "1.0.0-beta10",
    "CFBundleVersion": "702"
}
end