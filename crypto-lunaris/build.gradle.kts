import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.dokka")
    id("mooncloak.multiplatform")
}

kotlin {
    sourceSets {
        all {
            // Disable warnings and errors related to these expected @OptIn annotations.
            // See: https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.FlowPreview")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("-Xexpect-actual-classes")
        }

        val commonMain by getting {
            dependencies {
                implementation(project(":api-vpn"))
                implementation(project(":data-shared"))
                implementation(project(":data-sqlite"))

                // Http Client - Ktor
                // https://github.com/ktorio/ktor
                implementation("io.ktor:ktor-client-core:_")

                implementation(compose.runtime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(KotlinX.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                // Ethereum Cryptocurrencies - web3j
                // https://github.com/LFDT-web3j/web3j
                // Apache 2.0 License: https://github.com/LFDT-web3j/web3j?tab=License-1-ov-file#readme
                implementation("org.web3j:core:_")
            }
        }

        val jvmMain by getting {
            dependencies {
                // Ethereum Cryptocurrencies - web3j
                // https://github.com/LFDT-web3j/web3j
                // Apache 2.0 License: https://github.com/LFDT-web3j/web3j?tab=License-1-ov-file#readme
                implementation("org.web3j:core:_")
            }
        }
    }
}

dependencies {
    // Sometimes Android dependencies don't resolve in the androidMain source set dependencies block.

    // Android Cryptography Utilities
    implementation("androidx.security:security-crypto:_")
}

android {
    compileSdk = AppConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.vpn.crypto.lunaris"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            // Opt-in to experimental compose APIs
            freeCompilerArgs = listOf(
                "-Xopt-in=kotlin.RequiresOptIn"
            )
        }
    }
}
