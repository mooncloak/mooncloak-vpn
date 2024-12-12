import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.npm.tasks.KotlinNpmInstallTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

    explicitApi()

    sourceSets {
        all {
            // Disable warnings and errors related to these expected @OptIn annotations.
            // See: https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.FlowPreview")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("-Xexpect-actual-classes")
            languageSettings.optIn("androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi")
            languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        }

        val commonMain by getting {
            kotlin.srcDir("build/generated/ksp/commonMain/kotlin")

            dependencies {
                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.core)

                // Serialization
                // https://github.com/Kotlin/kotlinx.serialization
                implementation(KotlinX.serialization.json)

                // Time
                // https://github.com/Kotlin/kotlinx-datetime
                api(KotlinX.datetime)

                // Declarative UI - Compose Multiplatform
                api(compose.runtime)
                api(compose.foundation)
                api(compose.materialIconsExtended)

                // The Material(2) library is needed for some navigation components (Ex: to get the bottom navbar FAB
                // cutouts)
                implementation(compose.material)

                api(compose.material3)
                api(compose.material3AdaptiveNavigationSuite)

                // Resources
                implementation(compose.components.resources)

                // Navigation
                implementation("org.jetbrains.androidx.navigation:navigation-compose:_")

                // UI Glass Effect - Haze
                // https://chrisbanes.github.io/haze/
                implementation("dev.chrisbanes.haze:haze:_")
                implementation("dev.chrisbanes.haze:haze-materials:_")

                // UI Shaders - HypnoticCanvas
                // https://github.com/mikepenz/HypnoticCanvas
                // Apache 2.0: https://github.com/mikepenz/HypnoticCanvas/blob/dev/LICENSE
                implementation("com.mikepenz.hypnoticcanvas:hypnoticcanvas:_")

                // UI Window Size Classes - material3-windowsizeclass-multiplatform
                // https://github.com/chrisbanes/material3-windowsizeclass-multiplatform
                implementation("dev.chrisbanes.material3:material3-window-size-class-multiplatform:_")

                // statex - State Management Extensions
                // https://github.com/mooncloak/statex
                api("com.mooncloak.kodetools.statex:statex-core:_")
                api("com.mooncloak.kodetools.statex:statex-persistence:_")

                // Async Image Loading - coil
                // https://github.com/coil-kt/coil
                api("io.coil-kt.coil3:coil:_")
                api("io.coil-kt.coil3:coil-network-ktor3:_")
                implementation("io.coil-kt.coil3:coil-compose:_")

                // konstruct - Dependency Injection
                // https://github.com/mooncloak/konstruct
                api("com.mooncloak.kodetools.konstruct:konstruct-runtime:_")

                // Http Client - Ktor
                // https://github.com/ktorio/ktor
                api("io.ktor:ktor-client-core:_")
                api("io.ktor:ktor-client-content-negotiation:_")
                api("io.ktor:ktor-serialization-kotlinx-json:_")
                api("io.ktor:ktor-client-logging:_")
                implementation("io.ktor:ktor-client-encoding:_")

                // I/O - kotlinx-io
                // https://github.com/Kotlin/kotlinx-io
                // Required as a transitive dependency for something.
                api("org.jetbrains.kotlinx:kotlinx-io-core:_")

                // Core API models: apix
                // https://github.com/mooncloak/apix
                // Apache 2.0: https://github.com/mooncloak/apix/blob/main/LICENSE
                implementation("com.mooncloak.kodetools.apix:apix-core:_")

                // Key/Value Storage
                // https://github.com/mooncloak/storagex
                api("com.mooncloak.kodetools.storagex:storagex-keyvalue:_")

                // Logging
                // https://github.com/mooncloak/logpile
                api("com.mooncloak.kodetools.logpile:logpile-core:_")

                // Jetpack Compose Serializers - compose-serialization
                // https://github.com/mooncloak/compose-serialization
                // Apache 2.0: https://github.com/mooncloak/compose-serialization
                implementation("com.mooncloak.kodetools.compose.serialization:compose-serialization-core:_")

                // Pagination Utils - pagex
                // https://github.com/mooncloak/pagex
                // Apache 2.0: https://github.com/mooncloak/pagex/blob/main/LICENSE
                implementation("com.mooncloak.kodetools.pagex:pagex-core:_")

                // UI QR Codes - qrose
                // https://github.com/alexzhirkevich/qrose
                // MIT: https://github.com/alexzhirkevich/qrose/blob/main/LICENSE
                implementation("io.github.alexzhirkevich:qrose:_")

                // Dependency License Details - AboutLibraries
                // https://github.com/mikepenz/AboutLibraries
                // Apache 2.0: https://github.com/mikepenz/AboutLibraries/blob/develop/LICENSE
                implementation("com.mikepenz:aboutlibraries-compose-m3:_")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                // Coroutines - UI/Main Dispatcher
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.swing)

                implementation(compose.desktop.currentOs)

                // Http Client - Ktor
                // https://github.com/ktorio/ktor
                api("io.ktor:ktor-client-okhttp:_")

                // JVM Logging Abstraction - slf4j
                // https://www.slf4j.org/
                api("org.slf4j:slf4j-api:_")

                // JVM Logging Library - LogBack
                // https://logback.qos.ch/
                api("ch.qos.logback:logback-classic:_")
                api("ch.qos.logback:logback-core:_")

                // UI Previews
                implementation(compose.preview)
            }
        }

        val androidMain by getting {
            dependencies {
                // Http Client - Ktor
                // https://github.com/ktorio/ktor
                api("io.ktor:ktor-client-okhttp:_")
                api(Ktor.client.android)

                // Coroutines - UI/Main Dispatcher
                // https://github.com/Kotlin/kotlinx.coroutines
                implementation(KotlinX.coroutines.android)

                implementation(AndroidX.activity.compose)

                implementation(AndroidX.fragment.ktx)

                implementation(compose.material3AdaptiveNavigationSuite)

                // UI Previews
                implementation(compose.preview)

                // Allows opening of "Android Custom Tabs"
                // https://developer.chrome.com/docs/android/custom-tabs
                // https://developer.android.com/jetpack/androidx/releases/browser
                implementation("androidx.browser:browser:_")

                // Android Permission Handling
                // https://github.com/guolindev/PermissionX/blob/master/README.md
                implementation("com.guolindev.permissionx:permissionx:_")
            }
        }

        /*
        val iosMain by getting {
            dependencies {
                // Http Client - Ktor
                // https://github.com/ktorio/ktor
                api("io.ktor:ktor-client-darwin:_")

                implementation(compose.material3AdaptiveNavigationSuite)
            }
        }*/
    }
}

dependencies {
    // Annotation Processors:

    // konstruct - Dependency Injection
    // https://github.com/mooncloak/konstruct
    add("kspCommonMainMetadata", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
    add("kspJvm", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
    add("kspAndroid", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget = JvmTarget.JVM_11
}

// Don't run npm install scripts, protects against
// https://blog.jetbrains.com/kotlin/2021/10/important-ua-parser-js-exploit-and-kotlin-js/ etc.
tasks.withType<KotlinNpmInstallTask> {
    args += "--ignore-scripts"
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
}

android {
    compileSdk = AppConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.vpn.app.shared"

    defaultConfig {
        minSdk = AppConstants.Android.minSdkVersion
        targetSdk = AppConstants.Android.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            // Opt-in to experimental compose APIs
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].java.srcDirs("src/androidMain/kotlin")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    sourceSets["test"].java.srcDirs("src/androidTest/kotlin")
    sourceSets["test"].res.srcDirs("src/androidTest/res")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mooncloak.vpn.app.shared.resource"
    generateResClass = always
}


// `./gradlew :app-client-info:generateBuildKonfig`
buildkonfig {
    packageName = "com.mooncloak.vpn.feature.app"
    objectName = "SharedBuildConfig"

    defaultConfigs {
        buildConfigField(
            type = STRING,
            name = "group",
            value = buildVariables.group
        )
        buildConfigField(
            type = STRING,
            name = "appIdPrefix",
            value = buildVariables.idPrefix
        )
        buildConfigField(
            type = STRING,
            name = "appVersion",
            value = buildVariables.version
        )
        buildConfigField(
            type = INT,
            name = "appVersionCode",
            value = buildVariables.versionCode.toString()
        )
        buildConfigField(
            type = STRING,
            name = "appName",
            value = buildVariables.appName
        )
        buildConfigField(
            type = STRING,
            name = "appDescription",
            value = buildVariables.appDescription
        )
        buildConfigField(
            type = STRING,
            name = "appBuildTime",
            value = nowTimestamp()
        )
    }
}
