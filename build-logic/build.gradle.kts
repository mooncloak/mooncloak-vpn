plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(Kotlin.stdlib)
    implementation(Kotlin.gradlePlugin)

    // Environment variable access - kenv
    // https://github.com/mooncloak/kenv
    implementation("com.mooncloak.kodetools.kenv:kenv-core:_")

    // S3 Compatible Storage - minio
    // https://github.com/minio/minio-java
    implementation("io.minio:minio:_")
}

gradlePlugin {
    plugins.register("mooncloak.variables") {
        id = "mooncloak.variables"
        implementationClass = "BuildVariablesPlugin"
    }
}

gradlePlugin {
    plugins.register("mooncloak.publishWebApp") {
        id = "mooncloak.publishWebApp"
        implementationClass = "PublishWebAppPlugin"
    }
}
