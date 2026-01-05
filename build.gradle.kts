import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.0"
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
    implementation("com.google.guava:guava:33.5.0-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.1")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:6.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    targetCompatibility = "25"
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

application {
    mainClass.set("trivia.DiningPhilosophersKt")
}
