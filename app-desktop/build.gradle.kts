import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.UUID

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    id("mooncloak.variables")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":app-shared"))

    implementation(compose.runtime)
    implementation(compose.material3)
    implementation(compose.desktop.currentOs)
    implementation(compose.components.resources)

    // Http Client - Ktor
    // https://github.com/ktorio/ktor
    api("io.ktor:ktor-client-okhttp:_")

    // Annotation Processors:

    // konstruct - Dependency Injection
    // https://github.com/mooncloak/konstruct
    add("ksp", "com.mooncloak.kodetools.konstruct:konstruct-compiler-ksp:_")
}

tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.INHERIT }

compose.desktop {
    application {
        mainClass = "com.mooncloak.vpn.app.desktop.MainKt"

        // For more information on available settings, see the following documentation:
        // https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Native_distributions_and_local_execution
        nativeDistributions {
            // https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Native_distributions_and_local_execution#configuring-included-jdk-modules
            includeAllModules = true

            targetFormats(
                // macOS
                TargetFormat.Dmg,
                // FIXME: Not Working: TargetFormat.Pkg,
                // Windows
                TargetFormat.Exe,
                TargetFormat.Msi,
                // Linux
                TargetFormat.Deb,
                TargetFormat.Rpm
            )

            packageName = "${buildVariables.idPrefix}.desktop"
            packageVersion = buildVariables.strictVersionWithoutSuffix
            description = "mooncloak"
            copyright = "© mooncloak LLC. All rights reserved."
            vendor = "mooncloak"
            licenseFile.set(project.rootProject.file("LICENSE"))

            macOS {
                bundleID = "${buildVariables.idPrefix}.desktop"
                dockName = "mooncloak"
                appStore = false
                appCategory = "public.app-category.utilities"

                infoPlist {
                    val plistFile = project.layout.projectDirectory.file("src/main/resources/info_extras.plist").asFile

                    if (plistFile.exists()) {
                        extraKeysRawXml = plistFile.readText()
                    }
                }

                iconFile.set(project.rootProject.layout.projectDirectory.file("assets/icon/macos/AppIcon.icns"))
            }

            windows {
                console = true
                dirChooser = true
                perUserInstall = true
                upgradeUuid = UUID.randomUUID().toString()

                iconFile.set(project.rootProject.layout.projectDirectory.file("assets/icon/windows/AppIcon.ico"))
            }

            linux {
                debMaintainer = "developer@mooncloak.com"
                appRelease = buildVariables.versionCode.toString()
                appCategory = "vpn"

                iconFile.set(project.rootProject.layout.projectDirectory.file("assets/icon/linux/AppIcon.icns"))
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from(project.layout.projectDirectory.file("proguard-rules.pro"))
            obfuscate.set(true)
            joinOutputJars.set(true)
        }
    }
}
