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

    // Apache Commons
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.jetbrains:annotations:20.1.0")

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
