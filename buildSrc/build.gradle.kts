plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kotlin.jvm.plugin)
    implementation(libs.kotlinter.plugin)
    implementation(libs.detekt.plugin)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
