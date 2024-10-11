import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.10"
    application
}

group = "org.grutzmann"
version = "0.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.2")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.11.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    targetCompatibility = "21"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "21"
}

application {
    mainClass.set("pandemic.MainKt")
}
