plugins {
    id("java")
}

group = "me.aikoo.SphinxMassAnswerSender"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.15.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.15.0")
    implementation("org.seleniumhq.selenium:selenium-support:4.15.0")
    implementation("org.apache.poi:poi:5.2.4")
    implementation("org.apache.poi:poi-ooxml:5.2.4")
}

tasks.test {
    useJUnitPlatform()
}