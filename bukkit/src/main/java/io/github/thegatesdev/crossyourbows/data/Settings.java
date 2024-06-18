package io.github.thegatesdev.crossyourbows.data;

import org.bukkit.configuration.*;

import java.util.*;

public final class Settings {

    private final boolean requirePermission;
    private final boolean noArrowDamageCooldown; // TODO make a separate plugin for this, probably
    private final boolean noFireworkDamageCooldown;
    private final FireConfiguration defaultConfig;
    private final Map<String, FireConfiguration> namedConfigs;

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

    public Optional<FireConfiguration> defaultConfig() {
        return Optional.ofNullable(defaultConfig);
    }

    public Optional<FireConfiguration> namedConfig(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(namedConfigs.get(name));
    }

    public boolean hasNamedConfig(String name) {
        return namedConfigs.containsKey(name);
    }

    public Set<String> configNames() {
        return namedConfigs.keySet();
    }


    public static class Builder {
        private boolean requirePermission = false;
        private boolean noArrowDamageCooldown = false;
        private boolean noFireworkDamageCooldown = false;
        private FireConfiguration defaultConfiguration = null;
        private final Map<String, FireConfiguration> namedConfigs;


        public Builder() {
            namedConfigs = new HashMap<>();
        }

        public Builder(Builder other) {
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
            var configSections = conf.getConfigurationSection("fire_configurations");
            if (configSections != null) {
                var defaultSection = configSections.getConfigurationSection("default");
                var defaultBuilder = new FireConfiguration.Builder();
                if (defaultSection != null) defaultBuilder.load(defaultSection);
                defaultConfiguration(defaultBuilder.build());

                for (String sectionName : configSections.getKeys(false)) {
                    if (sectionName.equals("default")) continue;
                    var fireSection = configSections.getConfigurationSection(sectionName);
                    if (fireSection == null) continue;
                    var fireData = new FireConfiguration.Builder(defaultBuilder).load(fireSection);

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

        public Builder defaultConfiguration(FireConfiguration defaultConfiguration) {
            this.defaultConfiguration = defaultConfiguration;
            return this;
        }

        public Builder addNamedConfiguration(String name, FireConfiguration configuration) {
            namedConfigs.putIfAbsent(name, configuration);
            return this;
        }
    }
}
