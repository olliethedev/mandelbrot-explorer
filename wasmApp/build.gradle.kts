import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinNativeCompile

plugins {
    kotlin("multiplatform")
}

repositories {
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
}

val hostOs = System.getProperty("os.name")
val isWindows = hostOs.startsWith("Windows")

val packageName = "kotlinx.interop.wasm.dom"
val jsinteropKlibFile = buildDir.resolve("klib").resolve("$packageName-jsinterop.klib")

kotlin {
    wasm32("wasmApp") {
        binaries {
            executable {
                entryPoint = "sample.wasmApp.main"
            }
        }
    }
    sourceSets {
        val wasmAppMain by getting {
            dependencies {
                implementation(files(jsinteropKlibFile))
            }
        }
    }
}

val jsinterop by tasks.creating(Exec::class) {
    workingDir = projectDir

    val ext = if (isWindows) ".bat" else ""
    val distributionPath = project.properties["kotlin.native.home"] as String?

    if (distributionPath != null) {
        val jsinteropCommand = file(distributionPath).resolve("bin").resolve("jsinterop$ext")

        inputs.property("jsinteropCommand", jsinteropCommand)
        inputs.property("jsinteropPackageName", packageName)
        outputs.file(jsinteropKlibFile)

        commandLine(
            jsinteropCommand,
            "-pkg", packageName,
            "-o", jsinteropKlibFile,
            "-target", "wasm32"
        )
    } else {
        doFirst {
            // Abort build execution if the distribution path isn't specified.
            throw GradleException(
                """
                    |
                    |Kotlin/Native distribution path must be specified to build the JavaScript interop.
                    |Use 'kotlin.native.home' project property to specify it.
                """.trimMargin()
            )
        }
    }
}

tasks.withType(AbstractKotlinNativeCompile::class).all {
    dependsOn(jsinterop)
}

val assemble by tasks.getting

tasks.withType(KotlinJvmCompile::class).all {
    kotlinOptions.jvmTarget = "1.8"
}
