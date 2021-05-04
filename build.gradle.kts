plugins {
    java
}

group = "com.github"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    // Google Guava
    implementation("com.google.guava:guava:30.1.1-jre")

    implementation("org.jetbrains:annotations:20.1.0")

    // HPPC
    implementation("com.carrotsearch:hppc:0.9.0.RC2")

    // FastUtil
    implementation("it.unimi.dsi:fastutil:8.5.4")

    // JUnit Jupiter
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
    testLogging.showStandardStreams = true;
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
