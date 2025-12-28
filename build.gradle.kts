plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "dev.wiflow.viaflow"
version = providers.environmentVariable("VERSION").getOrElse("1.0.0")

labyMod {
    defaultPackageName = "dev.wiflow.viaflow"

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // devLogin = true
                    jvmArgs("-Xmx4G")
                    jvmArgs("-Xms2G")
                }
            }
        }
    }

    addonInfo {
        namespace = "viaflow"
        displayName = "ViaFlow"
        author = "wiflow"
        description = "Connect to any Minecraft server version from 1.8.9 to 1.21.x using ViaVersion protocol translation"
        minecraftVersion = "*"
        version = rootProject.version.toString()
    }
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
