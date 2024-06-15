package io.github.thegatesdev.crossyourbows.data;

import org.bukkit.configuration.*;

import java.util.*;

public final class FireConfiguration {
    private final boolean consumeItem;
    private final boolean pickupLastProjectile;
    private final int maxCharges;
    private final int fireCooldown;
    private final ProjectileSelection projectileSelection;
    private final ArrowSettings arrowSettings;
    private final CustomFiring firing;

    private FireConfiguration(boolean consumeItem, boolean pickupLastProjectile, int maxCharges, int fireCooldown, ProjectileSelection projectileSelection, ArrowSettings arrowSettings, CustomFiring firing) {
        this.consumeItem = consumeItem;
        this.pickupLastProjectile = pickupLastProjectile;
        this.maxCharges = maxCharges;
        this.fireCooldown = fireCooldown;
        this.projectileSelection = projectileSelection;
        this.arrowSettings = arrowSettings;
        this.firing = firing;
    }


    public boolean consumeItem() {
        return consumeItem;
    }

    public boolean pickupLastProjectile() {
        return pickupLastProjectile;
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
        private boolean pickupLastProjectile = true;
        private int maxCharges = 1;
        private int fireCooldown = 0;
        private ProjectileSelection projectileSelection = ProjectileSelection.BOTH;
        private ArrowSettings arrowSettings = null;
        private CustomFiring firing = null;

        public Builder() {
        }

        public Builder(Builder other) {
            this.consumeItem = other.consumeItem;
            this.pickupLastProjectile = other.pickupLastProjectile;
            this.maxCharges = other.maxCharges;
            this.fireCooldown = other.fireCooldown;
            this.projectileSelection = other.projectileSelection;
            this.arrowSettings = other.arrowSettings;
            this.firing = other.firing;
        }


        public FireConfiguration build() {
            return new FireConfiguration(consumeItem, pickupLastProjectile, maxCharges, fireCooldown, projectileSelection, arrowSettings, firing);
        }

        public Builder load(ConfigurationSection conf) {
            consumeItem(conf.getBoolean("consume_item", consumeItem));
            consumeItem(conf.getBoolean("pickup_last_projectile", pickupLastProjectile));
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

        public Builder pickupLastProjectile(boolean pickupLastProjectile) {
            this.pickupLastProjectile = pickupLastProjectile;
            return this;
        }

        public Builder maxCharges(int maxCharges) {
            this.maxCharges = maxCharges;
            return this;
        }

        public Builder fireCooldown(int fireCooldown) {
            this.fireCooldown = fireCooldown;
            return this;
        }

        public Builder projectileSelection(ProjectileSelection projectileSelection) {
            this.projectileSelection = Objects.requireNonNull(projectileSelection);
            return this;
        }

        public Builder arrowSettings(ArrowSettings arrowSettings) {
            this.arrowSettings = arrowSettings;
            return this;
        }

        public Builder firing(CustomFiring firing) {
            this.firing = Objects.requireNonNull(firing);
            return this;
        }
    }
}
