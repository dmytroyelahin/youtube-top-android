plugins {
    kotlin("jvm")
}

// Use the running JDK; no explicit toolchain to avoid provisioning issues

dependencies {
    testImplementation("junit:junit:4.13.2")
}
