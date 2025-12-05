plugins {
    id("kotlin-conventions")
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(project(":github-client"))
    implementation(libs.spring.boot.starter.webclient)
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
    implementation(libs.slf4j.api)
    implementation(libs.kotlinx.coroutines.reactor)
    runtimeOnly(libs.logback)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.assertj)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit.launcher)
}
