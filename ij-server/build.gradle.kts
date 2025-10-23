plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.2.21"
}

group = "com.github.azimsh3r.breakpointtrackeride"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}