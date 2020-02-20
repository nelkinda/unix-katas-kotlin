plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(project(":lib-test"))
    testImplementation("org.junit.jupiter", "junit-jupiter", "5.6.0")
}

tasks.test {
    useJUnitPlatform()
}
