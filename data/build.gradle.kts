plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}



java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(project(":domain"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation(libs.kotlinx.coroutines.core)
    implementation("javax.inject:javax.inject:1")

}