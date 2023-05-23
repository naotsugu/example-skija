plugins {
    application
}

repositories {
    mavenCentral()
}

val os = org.gradle.nativeplatform.platform.internal
        .DefaultNativePlatform.getCurrentOperatingSystem()
val arch = org.gradle.nativeplatform.platform.internal
        .DefaultNativePlatform.getCurrentArchitecture()
val artifact = when {
    os.isMacOsX  && arch.isArm64 -> "skija-macos-arm64"
    os.isMacOsX  && arch.isAmd64 -> "skija-macos-x64"
    os.isLinux   && arch.isAmd64 -> "skija-linux-x64"
    os.isWindows && arch.isAmd64 -> "skija-windows-x64"
    else -> throw Error("Unsupported OS: $os, ARCH: $arch")
}

dependencies {
    implementation("io.github.humbleui:${artifact}:0.109.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
    }
}

application {
    mainClass.set("com.mammb.code.example.skija.App")
}

