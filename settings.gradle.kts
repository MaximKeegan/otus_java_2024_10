rootProject.name = "otusJava"
include("HW01-gradle")
include("HW03-generics")
include("L06-annotations")
include("HW06-test-framework")
include("L08-gc:demo")
include("HW10-byteCode")

include("L12-solid")
include("L13-creationalPatterns")
include("L15-structuralPatterns:demo")
include("L15-structuralPatterns:homework")

include("L16-io:demo")
include("L16-io:homework")


pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}