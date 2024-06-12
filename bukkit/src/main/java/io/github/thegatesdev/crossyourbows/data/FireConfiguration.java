package io.github.thegatesdev.crossyourbows.data;

import org.bukkit.configuration.*;

import java.util.*;

public final class FireConfiguration {
    private final boolean consumeItem;
    private final int maxCharges;
    private final int fireCooldown;
    private final ProjectileSelection projectileSelection;
    private final ArrowSettings arrowSettings;
    private final CustomFiring firing;

    private FireConfiguration(boolean consumeItem, int maxCharges, int fireCooldown, ProjectileSelection projectileSelection, ArrowSettings arrowSettings, CustomFiring firing) {
        this.consumeItem = consumeItem;
        this.maxCharges = maxCharges;
        this.fireCooldown = fireCooldown;
        this.projectileSelection = projectileSelection;
        this.arrowSettings = arrowSettings;
        this.firing = firing;
    }


    public boolean consumeItem() {
        return consumeItem;
    }

    public int maxCharges() {
        return maxCharges;
    }

    public int fireCooldown() {
        return fireCooldown;
    }

    public ProjectileSelection projectileSelection() {
        return projectileSelection;
    }

    public Optional<ArrowSettings> arrowSettings() {
        return Optional.ofNullable(arrowSettings);
    }

    public Optional<CustomFiring> firing() {
        return Optional.ofNullable(firing);
    }


    public static class Builder {
        private boolean consumeItem = true;
        private int maxCharges = 1;
        private int fireCooldown = 0;
        private ProjectileSelection projectileSelection = ProjectileSelection.BOTH;
        private ArrowSettings arrowSettings = null;
        private CustomFiring firing = null;

        public Builder() {
        }

        public Builder(Builder other) {
            this.consumeItem = other.consumeItem;
            this.maxCharges = other.maxCharges;
            this.fireCooldown = other.fireCooldown;
            this.projectileSelection = other.projectileSelection;
            this.arrowSettings = other.arrowSettings;
            this.firing = other.firing;
        }


        public FireConfiguration build() {
            return new FireConfiguration(consumeItem, maxCharges, fireCooldown, projectileSelection, arrowSettings, firing);
        }

        public Builder load(ConfigurationSection conf) {
            consumeItem(conf.getBoolean("consume_item", consumeItem));
            maxCharges(conf.getInt("max_charges", maxCharges));
            fireCooldown(conf.getInt("fire_cooldown", fireCooldown));
            String selectionString = conf.getString("allow_projectile");
            if (selectionString != null) projectileSelection(ProjectileSelection.read(selectionString));
            ConfigurationSection arrowSettingsConf = conf.getConfigurationSection("arrow");
            if (arrowSettingsConf != null) arrowSettings(new ArrowSettings.Builder().load(arrowSettingsConf).build());
            // TODO custom firing load here
            return this;
        }


        public Builder consumeItem(boolean consumeItem) {
            this.consumeItem = consumeItem;
            return this;
        }

        public Builder maxCharges(int maxCharges) {
            this.maxCharges = maxCharges;
            return this;
        }

        public void fireCooldown(int fireCooldown) {
            this.fireCooldown = fireCooldown; // TODO fix builder returns
        }

        public Builder projectileSelection(ProjectileSelection projectileSelection) {
            this.projectileSelection = Objects.requireNonNull(projectileSelection);
            return this;
        }

        public void arrowSettings(ArrowSettings arrowSettings) {
            this.arrowSettings = arrowSettings;
        }

        public Builder firing(CustomFiring firing) {
            this.firing = Objects.requireNonNull(firing);
            return this;
        }
    }
}
