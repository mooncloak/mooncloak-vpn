import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

kotlin {
    androidTarget()

    sourceSets {
        val androidMain by getting {
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
            }
        }
    }
}

dependencies {
    // Annotation Processors:

    // konstruct - Dependency Injection
    // https://github.com/mooncloak/konstruct
    add("kspCommonMainMetadata", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
    add("kspAndroid", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
}

android {
    compileSdk = AppConstants.Android.compileSdkVersion
    namespace = "com.mooncloak.vpn.app.android"

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
        }

        create("direct") {
            dimension = "store"
            applicationIdSuffix = ".direct"
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

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].java.srcDirs("src/androidMain/kotlin")
    sourceSets["main"].res.srcDirs("src/androidMain/res")

    sourceSets["test"].java.srcDirs("src/androidTest/kotlin")
    sourceSets["test"].res.srcDirs("src/androidTest/res")
}

tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.INHERIT }
