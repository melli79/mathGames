import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.10"
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
    implementation("com.google.guava:guava:33.6.0-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("io.mockk:mockk:1.14.11")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.1")
    // testImplementation("org.openjdk.jol:jol-core:0.17")
    testRuntimeOnly("org.junit.platform:junit-platform-engine:6.1.1")
}

tasks.test {
    useJUnitPlatform()

    // jvmArgs("-Xmx16g")
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

//tasks.jar {
//    manifest {
//        attributes(
//            "Premain-Class" to "org.openjdk.jol.vm.InstrumentationSupport",
//            "Launcher-Agent-Class" to "org.openjdk.jol.vm.InstrumentationSupport"
//        )
//    }
//}
