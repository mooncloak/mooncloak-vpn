import io.minio.BucketExistsArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.SetBucketPolicyArgs
import io.minio.UploadObjectArgs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.io.File
import java.nio.file.Files

abstract class PublishWebAppPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.afterEvaluate {
            val isMultiplatformPluginApplied = target.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

            var supportsWebWasm = false

            if (isMultiplatformPluginApplied) {
                val multiplatformExtension = target.extensions.getByType<KotlinMultiplatformExtension>()

                multiplatformExtension.targets.forEach { kotlinTarget ->
                    when (kotlinTarget.platformType) {
                        KotlinPlatformType.wasm -> supportsWebWasm = true
                        else -> {}
                    }
                }
            }

            if (supportsWebWasm) {
                target.registerPublishWebApp()
            }
        }
    }
}

private fun Project.registerPublishWebApp() {
    val target = this

    target.tasks.register("publishWebApp") {
        group = "mooncloak"
        description = "Publishes the web app assets to the CDN."

        val accessKey = target.buildVariables.cdnPublishAccessKey ?: error("Required CDN Access Key is missing.")
        val secretKey = target.buildVariables.cdnPublishSecretKey ?: error("Required CDN Secret Key is missing.")

        doLast {
            publishFiles(
                accessKey = accessKey,
                secretKey = secretKey,
                version = target.buildVariables.version,
                isLatest = target.buildVariables.publishAsLatest,
                platform = "web",
                feature = target.name,
                assetDirectory = File(
                    target.layout.projectDirectory.asFile.path,
                    "build/dist/wasmJs/productionExecutable"
                )
            )

            // We also host the WASM binary files at the root web directory because the server will look there and not
            // have to filter for feature directories.
            publishFiles(
                accessKey = accessKey,
                secretKey = secretKey,
                version = target.buildVariables.version,
                isLatest = target.buildVariables.publishAsLatest,
                platform = "web",
                feature = null,
                assetDirectory = File(
                    target.layout.projectDirectory.asFile.path,
                    "build/dist/wasmJs/productionExecutable"
                ),
                filterFiles = { file ->
                    val lastPathSeparator = file.name.indexOfLast { char -> char == '/' }

                    if (lastPathSeparator != -1 && lastPathSeparator + 1 < file.name.length) {
                        val filename = file.name.substring(startIndex = lastPathSeparator + 1)

                        filename.startsWith("mooncloak-vpn") && filename.endsWith(".wasm")
                    } else {
                        false
                    }
                }
            )
        }

        dependsOn(target.tasks.findByName("wasmJsBrowserDistribution"))
    }
}

private fun publishFiles(
    accessKey: String,
    secretKey: String,
    version: String,
    isLatest: Boolean,
    platform: String? = null,
    feature: String? = null,
    assetDirectory: File,
    filterFiles: (FileToUpload) -> Boolean = { true }
) {
    val client = MinioClient.builder()
        .endpoint("https://nyc3.digitaloceanspaces.com/")
        .credentials(accessKey, secretKey)
        .build()

    if (!client.bucketExists(BucketExistsArgs.builder().bucket("mooncloak-cdn").build())) {
        client.makeBucket(MakeBucketArgs.builder().bucket("mooncloak-cdn").build())
    }

    if (!assetDirectory.exists()) {
        error("Missing distributable directory '${assetDirectory.path}'. Make sure to first build the project.")
    }

    val versionPrefix = createPrefix(
        version = version,
        platform = platform,
        feature = feature
    )
    val latestPrefix = createPrefix(
        version = "latest",
        platform = platform,
        feature = feature
    )

    assetDirectory.allFilesAsUploads(
        prefix = versionPrefix,
        includeDirectoryNameInPath = false,
        filter = filterFiles
    ).forEach { file ->
        val uploadBuilder = UploadObjectArgs.builder()
            .bucket("mooncloak-cdn")
            .`object`(file.name)
            .filename(file.filePath)

        if (file.mimeType.isNotBlank()) {
            uploadBuilder.contentType(file.mimeType)
        }

        client.uploadObject(
            uploadBuilder.build()
        )
    }

    if (isLatest) {
        assetDirectory.allFilesAsUploads(
            prefix = latestPrefix,
            includeDirectoryNameInPath = false,
            filter = filterFiles
        ).forEach { file ->
            val uploadBuilder = UploadObjectArgs.builder()
                .bucket("mooncloak-cdn")
                .`object`(file.name)
                .filename(file.filePath)

            if (file.mimeType.isNotBlank()) {
                uploadBuilder.contentType(file.mimeType)
            }

            client.uploadObject(
                uploadBuilder.build()
            )
        }
    }

    client.setBucketPolicy(
        SetBucketPolicyArgs.builder()
            .bucket("mooncloak-cdn")
            .config(storageAccessPolicy)
            .build()
    )
}

private fun File.allFilesAsUploads(
    prefix: String,
    includeDirectoryNameInPath: Boolean = true,
    filter: (FileToUpload) -> Boolean = { true }
): List<FileToUpload> =
    if (this.isDirectory) {
        (this.listFiles() ?: emptyArray())
            .flatMap { file ->
                file.allFilesAsUploads(
                    prefix = if (includeDirectoryNameInPath) {
                        "$prefix/${name.removePrefix("/")}/"
                    } else {
                        prefix.removePrefix("/")
                    }
                )
            }.filter { file -> filter.invoke(file) }
    } else {
        listOf(
            FileToUpload(
                name = "$prefix/$name",
                filePath = this.path,
                mimeType = this.mimeType()
            )
        ).filter { file -> filter.invoke(file) }
    }

private class FileToUpload(
    name: String,
    val filePath: String,
    val mimeType: String = ""
) {

    val name = name.removePrefix("/")
}

private val storageAccessPolicy = """
|{
|  "Version": "2012-10-17",
|  "Statement": [
|    {
|      "Sid": "PublicReadGetObject",
|      "Effect": "Allow",
|      "Principal": "*",
|      "Action": ["s3:GetObject"],
|      "Resource": ["arn:aws:s3:::mooncloak-cdn/*"]
|    }
|  ]
|}
""".trimMargin()

private fun File.mimeType(): String {
    val mimeType = runCatching { Files.probeContentType(this.toPath()) }.getOrNull()

    return when {
        mimeType != null -> mimeType
        this.name.endsWith("wasm") -> "application/wasm"
        else -> ""
    }
}

private fun createPrefix(
    version: String,
    platform: String? = null,
    feature: String? = null
): String =
    buildString {
        append("/app/")

        if (version.isBlank()) {
            error("'version' property is required.")
        }

        append("$version/")

        if (!platform.isNullOrBlank()) {
            append("$platform/")
        }

        if (!feature.isNullOrBlank()) {
            append("$feature/")
        }
    }
