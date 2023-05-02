import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
}

kotlin {
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("io.ktor:ktor-client-core:2.2.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
                implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
                implementation("io.ktor:ktor-client-json:2.2.4")
                implementation("io.ktor:ktor-client-serialization:2.2.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
                implementation("io.ktor:ktor-client-cio:2.2.4")
                implementation("io.ktor:ktor-client-logging:2.2.4")
                implementation(kotlin("reflect"))
                implementation("org.apache.pdfbox:pdfbox:2.0.24")

            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation("io.ktor:ktor-client-okhttp:2.2.4")
            }
        }

    }
}
//packageUberJarForCurrentOS
compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "com.involta.safeprint.desktopApp"
            packageVersion = "1.0.10"
            windows {
                shortcut = true
            }
        }
    }
}

