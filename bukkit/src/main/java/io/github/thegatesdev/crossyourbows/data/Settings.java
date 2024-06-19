package io.github.thegatesdev.crossyourbows.data;

import net.kyori.adventure.text.minimessage.*;
import org.bukkit.configuration.*;

import java.util.*;

public final class Settings {

    private final boolean requirePermission;
    private final boolean noArrowDamageCooldown; // TODO make a separate plugin for this, probably
    private final boolean noFireworkDamageCooldown;
    private final BowConfiguration defaultConfig;
    private final Map<String, BowConfiguration> namedConfigs;

    private Settings(Builder builder) {
        this.requirePermission = builder.requirePermission;
        this.noArrowDamageCooldown = builder.noArrowDamageCooldown;
        this.noFireworkDamageCooldown = builder.noFireworkDamageCooldown;
        this.defaultConfig = builder.defaultConfiguration;
        this.namedConfigs = new HashMap<>(builder.namedConfigs);
    }


    public boolean requirePermission() {
        return requirePermission;
    }

    public boolean noArrowDamageCooldown() {
        return noArrowDamageCooldown;
    }

    public boolean noFireworkDamageCooldown() {
        return noFireworkDamageCooldown;
    }

    public Optional<BowConfiguration> defaultConfig() {
        return Optional.ofNullable(defaultConfig);
    }

    public Optional<BowConfiguration> namedConfig(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(namedConfigs.get(name));
    }

    public Set<String> configNames() {
        return namedConfigs.keySet();
    }


    public static class Builder {

        private final MiniMessage miniMessage;
        private boolean requirePermission = false;
        private boolean noArrowDamageCooldown = false;
        private boolean noFireworkDamageCooldown = false;
        private BowConfiguration defaultConfiguration = null;
        private final Map<String, BowConfiguration> namedConfigs;


        public Builder(MiniMessage miniMessage) {
            this.miniMessage = miniMessage;
            namedConfigs = new HashMap<>();
        }

        public Builder(MiniMessage miniMessage, Builder other) {
            this.miniMessage = miniMessage;
            this.requirePermission = other.requirePermission;
            this.noArrowDamageCooldown = other.noArrowDamageCooldown;
            this.noFireworkDamageCooldown = other.noFireworkDamageCooldown;
            this.defaultConfiguration = other.defaultConfiguration;
            this.namedConfigs = new HashMap<>(other.namedConfigs);
        }


        public Settings build() {
            return new Settings(this);
        }

        public Builder load(ConfigurationSection conf) {
            requirePermission(conf.getBoolean("require_permission", requirePermission));
            noArrowDamageCooldown(!conf.getBoolean("arrow_damage_cooldown", noArrowDamageCooldown));
            noFireworkDamageCooldown(!conf.getBoolean("firework_damage_cooldown", noFireworkDamageCooldown));
            var configSections = conf.getConfigurationSection("bow_configurations");
            if (configSections != null) {
                var defaultSection = configSections.getConfigurationSection("default");
                var defaultBuilder = new BowConfiguration.Builder(miniMessage);
                if (defaultSection != null) defaultBuilder.load(defaultSection);
                defaultConfiguration(defaultBuilder.build());

                for (String sectionName : configSections.getKeys(false)) {
                    if (sectionName.equals("default")) continue;
                    var fireSection = configSections.getConfigurationSection(sectionName);
                    if (fireSection == null) continue;
                    var fireData = new BowConfiguration.Builder(miniMessage, defaultBuilder).load(fireSection);

                    addNamedConfiguration(sectionName.toLowerCase(), fireData.build());
                }
            }

            return this;
        }


        public Builder requirePermission(boolean requirePermission) {
            this.requirePermission = requirePermission;
            return this;
        }

        public Builder noArrowDamageCooldown(boolean noArrowDamageCooldown) {
            this.noArrowDamageCooldown = noArrowDamageCooldown;
            return this;
        }

        public Builder noFireworkDamageCooldown(boolean noFireworkDamageCooldown) {
            this.noFireworkDamageCooldown = noFireworkDamageCooldown;
            return this;
        }

        public Builder defaultConfiguration(BowConfiguration defaultConfiguration) {
            this.defaultConfiguration = defaultConfiguration;
            return this;
        }

        public Builder addNamedConfiguration(String name, BowConfiguration configuration) {
            namedConfigs.putIfAbsent(name, configuration);
            return this;
        }
    }
}
