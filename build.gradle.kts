import xyz.jpenilla.resourcefactory.bukkit.Permission
import xyz.jpenilla.resourcefactory.bukkit.bukkitPluginYaml

plugins {
    java
    id("xyz.jpenilla.resource-factory") version "1.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "io.github.thegatesdev"
version = "1.1"
description = "Crossbow ammunition control"


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


val bukkitYaml = bukkitPluginYaml {
    main = "io.github.thegatesdev.ammo.control.AmmoControl"
    author = "thegatesdev"
    apiVersion = "1.20"

    commands {
        create("ammocontrol") {
            description = "The AmmoControl command"
            usage = "/ammocontrol <argument>"
            permission = "ammocontrol.command"
        }
    }

    defaultPermission = Permission.Default.FALSE
    permissions {
        // Command
        create("ammocontrol.command") {
            description = "Allows the use of the /ammocontrol command"
        }
        create("ammocontrol.command.reload") {
            description = "Allows the use of the /ammocontrol reload command"
            children("ammocontrol.command")
        }

        // Main controls
        create("ammocontrol.enable.all") {
            description = "Enables the firework and arrow options"
            children(
                "ammocontrol.enable.fireworks",
                "ammocontrol.enable.arrows",
            )
        }
        create("ammocontrol.enable.fireworks") {
            description = "Enables the firework options"
        }
        create("ammocontrol.enable.arrows") {
            description = "Enables the arrow options"
        }

        // Bypasses
        create("ammocontrol.bypass.all") {
            description = "Bypass the firework and arrow fire cooldown and limit"
            children(
                "ammocontrol.bypass.fireworks.all",
                "ammocontrol.bypass.arrows.all",
            )
        }
        // -- Bypass fireworks
        create("ammocontrol.bypass.fireworks.all") {
            description = "Bypass the firework cooldown and limit"
            children(
                "ammocontrol.bypass.fireworks.cooldown",
                "ammocontrol.bypass.fireworks.limit",
            )
        }
        create("ammocontrol.bypass.fireworks.cooldown") {
            description = "Bypass the firework fire cooldown"
        }
        create("ammocontrol.bypass.fireworks.limit") {
            description = "Bypass the firework fire limit"
        }
        // -- Bypass arrows
        create("ammocontrol.bypass.arrows.all") {
            description = "Bypass the arrow cooldown and limit"
            children(
                "ammocontrol.bypass.arrows.cooldown",
                "ammocontrol.bypass.arrows.limit",
            )
        }
        create("ammocontrol.bypass.arrows.cooldown") {
            description = "Bypass the arrow fire cooldown"
        }
        create("ammocontrol.bypass.arrows.limit") {
            description = "Bypass the arrow fire limit"
        }
    }
}

sourceSets.main {
    resourceFactory {
        factory(bukkitYaml.resourceFactory())
    }
}