import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.github.alyssaruth"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.6.1")

    testImplementation(kotlin("test"))

    testImplementation("io.kotest:kotest-assertions-core:5.5.4")
}

tasks.test {
    minHeapSize = "1024m"
    maxHeapSize = "1024m"

    useJUnitPlatform()

    testLogging {
        events = mutableSetOf(TestLogEvent.STARTED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }
}
kotlin {
    jvmToolchain(17)
}