rootProject.name = "viaflow"

pluginManagement {
    val labyGradlePluginVersion = "0.5.9"
    buildscript {
        repositories {
            maven("https://dist.labymod.net/api/v1/maven/release/")
            maven("https://maven.neoforged.net/releases/")
            maven("https://maven.fabricmc.net/")
            maven("https://repo.viaversion.com/")
            maven("https://repo.lenni0451.net/releases/")
            maven("https://repo.lenni0451.net/snapshots/")
            gradlePluginPortal()
            mavenCentral()
        }

        dependencies {
            classpath("net.labymod.gradle", "common", labyGradlePluginVersion)
        }
    }

    // Plugin repositories for shadow plugin
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins.apply("net.labymod.labygradle.settings")

include(":api")
include(":core")
include(":game-runner")
