import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm") version "2.3.0"
}

group = "com.github.alyssaruth"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.7.1")

    testImplementation(kotlin("test"))

    testImplementation("io.kotest:kotest-assertions-core:6.1.11")
}

tasks.register("solve", JavaExec::class) {
    configure(
        closureOf<JavaExec> {
            group = "run"
            classpath = project.the<SourceSetContainer>()["main"].runtimeClasspath
            mainClass.set("EntrypointKt")
            minHeapSize = "1024m"
            maxHeapSize = "1024m"

            val puzzleNumber = properties["uzzle"]
            if (puzzleNumber != null) {
                args(puzzleNumber)
            }
        }
    )
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
    jvmToolchain(25)
}