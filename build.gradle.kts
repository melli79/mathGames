import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.0-RC2"
    application
}

group = "org.grutzmann"
version = "0.2-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    test {
        kotlin.srcDir("src/test/kotlin")
    }
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

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

application {
    mainClass.set("trivia.DiningPhilosophersKt")
}
