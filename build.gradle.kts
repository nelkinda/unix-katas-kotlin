plugins {
    kotlin("jvm") version "1.3.61" apply false
}

allprojects {
    group = "com.nelkinda.training"
    version = "0.0.1-SNAPSHOT"
    repositories {
        jcenter()
    }
}

tasks.wrapper {
    gradleVersion = "6.2"
    jarFile = File(".gradle/wrapper/gradle-wrapper.jar")
}
