plugins {
    id("kotlin-conventions")
    `java-library`
}

dependencies {
    api(libs.jackson.annotations)
    api(libs.kotlinx.coroutines.core)
    api(libs.spring.webflux)

    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.slf4j.api)

    testImplementation(libs.assertj)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.mockwebserver)

    testRuntimeOnly(libs.junit.launcher)
    testRuntimeOnly(libs.logback)
}
