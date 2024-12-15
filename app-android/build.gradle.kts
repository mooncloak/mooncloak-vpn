import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("android")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

kotlin {

}

dependencies {
    implementation(project(":app-shared"))

    // Coroutines Android Lifecycle
    implementation(AndroidX.lifecycle.runtime.ktx)

    implementation(AndroidX.core.splashscreen)

    implementation(AndroidX.activity.compose)
    implementation(compose.ui)

    // Android support libraries.
    api(AndroidX.appCompat)

    // UI Widgets - Compose Glance
    implementation("androidx.glance:glance-appwidget:_")
    implementation("androidx.glance:glance-material3:_")

    // Async Image Loading - coil
    // https://github.com/coil-kt/coil
    implementation("io.coil-kt.coil3:coil:_")
    implementation("io.coil-kt.coil3:coil-network-ktor:_")
    implementation("io.coil-kt.coil3:coil-network-ktor3:_")

    // VPN - WireGuard
    // https://github.com/WireGuard/wireguard-android
    // https://github.com/WireGuard/wireguard-android/blob/master/COPYING
    implementation("com.wireguard.android:tunnel:_")

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

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            isMinifyEnabled = false
        }
    }

    flavorDimensions += "store"

    productFlavors {
        create("play") {
            dimension = "store"
            applicationIdSuffix = ".play"

            resValue("string", "mooncloak_app_name", buildVariables.appName)
        }

        create("direct") {
            dimension = "store"
            applicationIdSuffix = ".direct"

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
