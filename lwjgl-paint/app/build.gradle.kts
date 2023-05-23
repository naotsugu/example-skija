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

val lwjglNatives = when {
    os.isMacOsX -> "natives-macos"
    os.isLinux -> "natives-linux"
    os.isWindows -> "natives-windows"
    else -> throw Error("Unsupported OS: $os")
}


dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:3.3.2"))
    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-opengl")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)

    implementation("io.github.humbleui:${artifact}:0.109.2")
    implementation("io.github.humbleui:skija-shared:0.109.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val exportsOpt = arrayOf("-XstartOnFirstThread")
application {
    mainClass.set("com.mammb.code.example.skija.App")
    applicationDefaultJvmArgs = listOf("-XstartOnFirstThread")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

