import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":app-shared"))

    // Android support libraries.
    api(AndroidX.appCompat)

    implementation(AndroidX.core.splashscreen)

    // Coroutines Android Lifecycle
    implementation(AndroidX.lifecycle.runtime.ktx)

    implementation(AndroidX.activity.compose)

    implementation(compose.ui)

    // UI Widgets - Compose Glance
    implementation("androidx.glance:glance-appwidget:_")
    implementation("androidx.glance:glance-material3:_")

    // Async Image Loading - coil
    // https://github.com/coil-kt/coil
    implementation("io.coil-kt.coil3:coil:_")
    implementation("io.coil-kt.coil3:coil-network-ktor:_")
    implementation("io.coil-kt.coil3:coil-network-ktor3:_")

    // Android Cryptography
    implementation("androidx.security:security-crypto:_")

    // VPN - WireGuard
    // https://github.com/WireGuard/wireguard-android
    // https://github.com/WireGuard/wireguard-android/blob/master/COPYING
    implementation("com.wireguard.android:tunnel:_")

    // Google Shortcuts - Adds App Shortcuts as Google Assistant Commands
    // https://developer.android.com/develop/ui/views/launch/shortcuts/creating-shortcuts#dynamic
    // Only applied for the Google Play Build
    // FIXME: Configuration not found: "playImplementation"("androidx.core:core-google-shortcuts:_")

    // Annotation Processors:

    // konstruct - Dependency Injection
    // https://github.com/mooncloak/konstruct
    ksp("com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
}

android {
    compileSdk = AppConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.vpn.app.android"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.mooncloak.vpn.app.android"
        version = buildVariables.version
        versionCode = buildVariables.versionCode
        minSdk = AppConstants.Android.minSdkVersion
        targetSdk = AppConstants.Android.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("playRelease") {
            storeFile = project.rootProject.layout.projectDirectory.file(".keystore/upload-key.jks").asFile
            storePassword = buildVariables.googlePlaySigningKeyStorePassword
            keyAlias = buildVariables.googlePlaySigningKeyAlias
            keyPassword = buildVariables.googlePlaySigningKeyPassword
        }
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            // TODO: Re-enable debug app suffix: applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    flavorDimensions += "store"

    productFlavors {
        val playSigningConfig = signingConfigs.getByName("playRelease")

        create("play") {
            dimension = "store"
            applicationIdSuffix = ".play"

            signingConfig = playSigningConfig

            resValue("string", "mooncloak_app_name", buildVariables.appName)
        }

        create("direct") {
            dimension = "store"
            applicationIdSuffix = ".direct"

            resValue("string", "mooncloak_app_name", buildVariables.appName)
        }

        create("galaxy") {
            dimension = "store"
            applicationIdSuffix = ".galaxy"

            resValue("string", "mooncloak_app_name", buildVariables.appName)
        }

        create("fdroid") {
            dimension = "store"
            applicationIdSuffix = ".fdroid"

            resValue("string", "mooncloak_app_name", buildVariables.appName)
        }
    }

    dependencies {
        // Enables Java 8 APIs for Android - Required by the WireGuard dependency
        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:_")

        // Google Play Billing - Required for payments for apps installed via Google Play
        // https://developer.android.com/google/play/billing
        "playImplementation"("com.android.billingclient:billing-ktx:_")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            // Opt-in to experimental compose APIs
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }
}

tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.INHERIT }
