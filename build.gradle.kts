import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.vsloong"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    // 多模匹配算法aho-corasick
    commonMainImplementation("com.hankcs:aho-corasick-double-array-trie:1.2.2")

    // 依赖注入
    commonMainImplementation("io.insert-koin:koin-core:3.4.0")

    // 解压缩
    commonMainImplementation("org.apache.commons:commons-compress:1.22")

    // jadx
    commonMainImplementation("io.github.skylot:jadx-core:1.4.7")
    commonMainImplementation("io.github.skylot:jadx-dex-input:1.4.7")
    commonMainImplementation("ch.qos.logback:logback-classic:1.4.7")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ApkNurse"
            packageVersion = "1.0.0"
        }
    }
}
