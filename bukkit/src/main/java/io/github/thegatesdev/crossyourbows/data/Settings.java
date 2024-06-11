package io.github.thegatesdev.crossyourbows.data;

import org.bukkit.configuration.*;

import java.util.*;

public final class Settings {

    private final boolean requirePermission;
    private final FireConfiguration defaultConfig;
    private final Map<String, FireConfiguration> namedConfigs;

    private Settings(boolean requirePermission, FireConfiguration defaultConfig, Map<String, FireConfiguration> namedConfigs) {
        this.requirePermission = requirePermission;
        this.defaultConfig = defaultConfig;
        this.namedConfigs = namedConfigs;
    }


    public boolean requirePermission() {
        return requirePermission;
    }

    public Optional<FireConfiguration> defaultConfig() {
        return Optional.ofNullable(defaultConfig);
    }

    public Optional<FireConfiguration> namedConfig(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(namedConfigs.get(name));
    }


    public static class Builder {
        private boolean requirePermission = false;
        private FireConfiguration defaultConfiguration = null;
        private final Map<String, FireConfiguration> namedConfigs;


        public Builder() {
            namedConfigs = new HashMap<>();
        }

        public Builder(Builder other) {
            this.requirePermission = other.requirePermission;
            this.defaultConfiguration = other.defaultConfiguration;
            this.namedConfigs = new HashMap<>(other.namedConfigs);
        }


        public Settings build() {
            return new Settings(requirePermission, defaultConfiguration, new HashMap<>(namedConfigs));
        }

        public Builder load(ConfigurationSection conf) {
            requirePermission(conf.getBoolean("require_permission", requirePermission));
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
