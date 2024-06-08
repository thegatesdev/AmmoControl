package io.github.thegatesdev.ammo.control.config;

import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class AmmoConfig {

    private final Path location;
    private Settings activeSettings;

    public AmmoConfig(Path location) {
        this.location = location;
    }


    public Optional<Settings> getActiveSettings() {
        return Optional.ofNullable(activeSettings);
    }

    public boolean load() {
        FileConfiguration confData;
        try (var reader = Files.newBufferedReader(location)) {
            confData = YamlConfiguration.loadConfiguration(reader);
        } catch (IOException ioException) {
            return false;
        }

        activeSettings = create(confData);
        return true;
    }


    private static Settings create(FileConfiguration conf) {
        ConfigurationSection secFireworks = sectionOrEmpty(conf, "fireworks");
        ConfigurationSection secArrows = sectionOrEmpty(conf, "arrows");

        return new Settings(
                conf.getBoolean("printDebug", false),
                conf.getBoolean("disableTabCompletion", false),
                conf.getBoolean("simpleProjectileCheck", false),
                ControlMode.of(secFireworks.getString("mode", "disabled")),
                ControlMode.of(secArrows.getString("mode", "disabled")),
                secFireworks.getDouble("cooldown", 0d),
                secArrows.getDouble("cooldown", 0d),
                secFireworks.getInt("limit", 0),
                secArrows.getInt("limit", 0)
        );
    }

    private static ConfigurationSection sectionOrEmpty(ConfigurationSection section, String path) {
        var value = section.getConfigurationSection(path);
        return value == null ? new MemoryConfiguration() : value;
    }


    public record Settings(
            boolean printDebug,
            boolean disableTabCompletion,
            boolean simpleProjectileCheck,
            ControlMode fireworkMode,
            ControlMode arrowMode,
            double fireworkCooldown,
            double arrowCooldown,
            int fireworkLimit,
            int arrowLimit
    ) {
        public Settings() {
            this(false, false, true, ControlMode.DISABLED, ControlMode.DISABLED, 0d, 0d, 0, 0);
        }
    }

    public enum ControlMode {
        INVALID,
        DISABLED,
        KEEP_ITEM,
        KEEP_BOW;

        public static ControlMode of(String name) {
            return switch (name.toLowerCase()) {
                case "disabled" -> DISABLED;
                case "keep_item" -> KEEP_ITEM;
                case "keep_bow" -> KEEP_BOW;
                default -> INVALID;
            };
        }
    }
}
