repositories {
    maven("https://repo.viaversion.com/")
    maven("https://repo.lenni0451.net/releases/")
    maven("https://repo.lenni0451.net/snapshots/")
    maven("https://libraries.minecraft.net/")
}

dependencies {
    // Core module
    api(project(":core"))
}

// Fix Guava conflict for MC 1.8.9: Force newer authlib compiled against Guava 18+
// The bundled MC 1.8.9 authlib was compiled against Guava 17, causing VerifyError
val v1_8_9 = configurations.findByName("v1_8_9Implementation")
if (v1_8_9 != null) {
    dependencies {
        // Use authlib 1.5.25 which was compiled against Guava 18+
        "v1_8_9Implementation"("com.mojang:authlib:1.5.25")
        // SLF4J required by ViaLoader (not bundled in older MC versions)
        "v1_8_9Implementation"("org.slf4j:slf4j-api:2.0.9")
        "v1_8_9Implementation"("org.slf4j:slf4j-simple:2.0.9")
    }

    // Force the newer authlib version
    configurations.named("v1_8_9RuntimeClasspath") {
        resolutionStrategy {
            force("com.mojang:authlib:1.5.25")
            force("com.google.guava:guava:21.0")
        }
    }
}
