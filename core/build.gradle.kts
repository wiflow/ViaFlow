import net.labymod.labygradle.common.extension.LabyModAnnotationProcessorExtension.ReferenceType

dependencies {
    labyProcessor()
    api(project(":api"))

    addonMavenDependency("com.viaversion:viaversion-common:5.12.0")
    addonMavenDependency("com.viaversion:viabackwards-common:5.12.0")
    addonMavenDependency("com.viaversion:viarewind-common:4.2.0")

    // Provided by Minecraft at runtime. 1.8.9 and 1.12.2 ship Netty 4.0, so 4.1-only API
    // must stay inside classes that are only loaded on newer versions.
    compileOnly("io.netty:netty-all:4.1.118.Final")

    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.13.4")
    testRuntimeOnly("com.google.guava:guava:17.0")
}

labyModAnnotationProcessor {
    referenceType = ReferenceType.DEFAULT
}

// LabyMod's annotation processor only works on the main source set.
tasks.compileTestJava {
    options.compilerArgs.add("-proc:none")
}

tasks.test {
    useJUnitPlatform()
    systemProperty(
        "viaflow.minecraftVersions",
        providers.gradleProperty("net.labymod.minecraft-versions").get()
    )
}
