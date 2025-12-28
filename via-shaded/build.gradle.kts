plugins {
    `java-library`
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

repositories {
    maven("https://repo.viaversion.com/")
    maven("https://repo.lenni0451.net/releases/")
    maven("https://repo.lenni0451.net/snapshots/")
    mavenCentral()
}

dependencies {
    api("com.viaversion:vialoader:4.0.6")
    api("com.viaversion:viaversion-common:5.7.0-SNAPSHOT")
    api("com.viaversion:viabackwards-common:5.7.0-SNAPSHOT")
    api("com.viaversion:viarewind-common:4.0.12")
    api("net.raphimc:ViaLegacy:3.0.11") {
        exclude(group = "com.google.code.gson", module = "gson")
    }
    // SLF4J for 1.8.9 compatibility (not included in old MC versions)
    api("org.slf4j:slf4j-api:2.0.9")
    api("org.slf4j:slf4j-simple:2.0.9")
}

tasks.shadowJar {
    archiveClassifier.set("")
    mergeServiceFiles()
    relocate("com.google.common", "dev.wiflow.viaflow.libs.guava")
    relocate("com.google.thirdparty", "dev.wiflow.viaflow.libs.thirdparty")
    relocate("org.slf4j", "dev.wiflow.viaflow.libs.slf4j")
    exclude("META-INF/maven/**")
    exclude("META-INF/versions/**")
    exclude("META-INF/LICENSE*")
    exclude("META-INF/NOTICE*")
    exclude("io/netty/**")
}

tasks.jar {
    enabled = false
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
