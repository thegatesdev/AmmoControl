plugins {
    java
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "io.github.thegatesdev"
version = "1.1"
description = "Minecraft plugin for extra crossbow settings"


repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}


tasks {
    runServer {
        minecraftVersion("1.20.4")
        legacyPluginLoading = false
    }
}

bukkitPluginYaml {
    main = "io.github.thegatesdev.crossyourbows.CrossYourBowsBukkit"
    author = "thegatesdev"
    apiVersion = "1.19"
    description = "Crossbows have never been this powerful!"
}