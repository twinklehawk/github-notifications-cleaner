plugins {
    kotlin("jvm")
    jacoco
    id("org.jmailen.kotlinter")
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    mavenCentral()
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.detekt {
    enabled = false
}
tasks.detektMain {
    buildUponDefaultConfig = true
    config.setFrom("../detekt-config.yaml",)
}
tasks.detektTest {
    buildUponDefaultConfig = true
    config.setFrom("../detekt-config.yaml")
}
tasks.check {
    dependsOn("detektMain", "detektTest")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
dependencies {
    implementation(platform(libs.findLibrary("spring-boot-bom").get()))
    implementation(platform(libs.findLibrary("kotlin-bom").get()))
    implementation(platform(libs.findLibrary("kotlinx-coroutines-bom").get()))
}
