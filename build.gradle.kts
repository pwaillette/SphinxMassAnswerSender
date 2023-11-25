plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "me.aikoo.sphinxmassanswersender"
version = "1.0.0"

repositories {
    mavenCentral()
}

// Required by the 'shadowJar' task
project.setProperty("mainClassName", "me.aikoo.sphinxmassanswersender.Main")

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

tasks {
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "me.aikoo.sphinxmassanswersender.Main"
    }

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set("sphinxmassanswersender")
    archiveClassifier.set("")
    archiveVersion.set("")
}


tasks.test {
    useJUnitPlatform()
}