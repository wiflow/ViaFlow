import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

plugins {
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

repositories {
    maven("https://repo.viaversion.com/")
    maven("https://repo.lenni0451.net/releases/")
    maven("https://repo.lenni0451.net/snapshots/")
    mavenCentral()
}

val shadedVia: Configuration by configurations.creating

dependencies {
    labyProcessor()
    api(project(":api"))

    // Netty (provided by Minecraft at runtime)
    compileOnly("io.netty:netty-all:4.1.97.Final")

    // Use the shaded ViaVersion module with Guava relocated
    val shadedJar = rootProject.file("via-shaded/build/libs/via-shaded.jar")
    api(files(shadedJar))
    shadedVia(files(shadedJar))
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}

tasks.jar {
    dependsOn(tasks.shadowJar)
    enabled = false
}

tasks.shadowJar {
    configurations = listOf(shadedVia)
    archiveClassifier.set("")
}
