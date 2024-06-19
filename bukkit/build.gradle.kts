import xyz.jpenilla.resourcefactory.bukkit.Permission

plugins {
    java
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "io.github.thegatesdev"
version = "2024.6.19"
description = "Minecraft plugin for extra crossbow settings"


repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}


tasks {
    runServer {
        minecraftVersion("1.20.6")
        legacyPluginLoading = false
    }
    jar {
        archiveBaseName = "CrossYourBows-Bukkit"
        manifest {
            // We don't use NMS... disable remapping
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }
}

bukkitPluginYaml {
    name = "CrossYourBows"
    main = "io.github.thegatesdev.crossyourbows.CrossYourBowsBukkit"
    author = "thegatesdev"
    apiVersion = "1.20.6"
    description = "Crossbows have never been this powerful!"

    commands {
        create("crossyourbows") {
            permission = "crossyourbows.command"
            description = "CrossYourBows command"
            usage = "/crossyourbows (reload|apply|reset) <args>"

            aliases.add("cb")
        }
    }

    permissions {
        defaultPermission = Permission.Default.OP

        create("crossyourbows.command") {
            description = "Allows the usage of the /crossyourbows command"
        }
        create("crossyourbows.usage") {
            description = "Enables custom bow settings"
            default = Permission.Default.FALSE
        }
    }
}