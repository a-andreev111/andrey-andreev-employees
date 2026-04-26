plugins {
    java
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.46"
val junitVersion = "5.10.2"

dependencies {
    // Main
    implementation("org.apache.commons:commons-lang3:3.20.0")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    // Test
    testImplementation("org.junit.jupiter:junit-jupiter:$junitVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:5.23.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

javafx {
    version = "25"
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("org.example.ui.MainApp")
}

tasks.test {
    useJUnitPlatform()
}