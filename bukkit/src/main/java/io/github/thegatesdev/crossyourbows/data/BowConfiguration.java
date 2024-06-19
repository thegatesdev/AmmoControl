package io.github.thegatesdev.crossyourbows.data;

import net.kyori.adventure.text.minimessage.*;
import org.bukkit.configuration.*;

import java.util.*;

public final class BowConfiguration {

    private final boolean consumeItem;
    private final boolean pickupLastProjectile;
    private final int maxCharges;
    private final int fireCooldown;
    private final ProjectileSelection allowProjectile;
    private final ProjectileSelection enableProjectile;
    private final DisplaySettings displaySettings;
    private final ArrowSettings arrowSettings;
    private final CustomFiring firing;

    private BowConfiguration(Builder builder) {
        this.consumeItem = builder.consumeItem;
        this.pickupLastProjectile = builder.pickupLastProjectile;
        this.maxCharges = builder.maxCharges;
        this.fireCooldown = builder.fireCooldown;
        this.allowProjectile = builder.allowProjectile;
        this.enableProjectile = builder.enableProjectile;
        this.displaySettings = builder.displaySettings;
        this.arrowSettings = builder.arrowSettings;
        this.firing = builder.firing;
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

    public ProjectileSelection allowProjectile() {
        return allowProjectile;
    }

    public ProjectileSelection enableProjectile() {
        return enableProjectile;
    }

    public Optional<DisplaySettings> displaySettings() {
        return Optional.ofNullable(displaySettings);
    }

    public Optional<ArrowSettings> arrowSettings() {
        return Optional.ofNullable(arrowSettings);
    }

    public Optional<CustomFiring> firing() {
        return Optional.ofNullable(firing);
    }


    public static class Builder {
        private final MiniMessage miniMessage;
        private boolean consumeItem = true;
        private boolean pickupLastProjectile = true;
        private int maxCharges = 1;
        private int fireCooldown = 0;
        private ProjectileSelection allowProjectile = ProjectileSelection.BOTH;
        private ProjectileSelection enableProjectile = ProjectileSelection.BOTH;
        private DisplaySettings displaySettings = null;
        private ArrowSettings arrowSettings = null;
        private CustomFiring firing = null;

        public Builder(MiniMessage miniMessage) {
            this.miniMessage = miniMessage;
        }

        public Builder(MiniMessage miniMessage, Builder other) {
            this.miniMessage = miniMessage;
            this.consumeItem = other.consumeItem;
            this.pickupLastProjectile = other.pickupLastProjectile;
            this.enableProjectile = other.enableProjectile;
            this.maxCharges = other.maxCharges;
            this.fireCooldown = other.fireCooldown;
            this.allowProjectile = other.allowProjectile;
            this.displaySettings = other.displaySettings;
            this.arrowSettings = other.arrowSettings;
            this.firing = other.firing;
        }


        public BowConfiguration build() {
            return new BowConfiguration(this);
        }

        public Builder load(ConfigurationSection conf) {
            consumeItem(conf.getBoolean("consume_item", consumeItem));
            consumeItem(conf.getBoolean("pickup_last_projectile", pickupLastProjectile));
            maxCharges(conf.getInt("max_charges", maxCharges));
            fireCooldown(conf.getInt("fire_cooldown", fireCooldown));

            String selectionString = conf.getString("allow_projectile");
            if (selectionString != null) allowProjectile(ProjectileSelection.read(selectionString));
            selectionString = conf.getString("enable_projectile");
            if (selectionString != null) enableProjectile(ProjectileSelection.read(selectionString));

            ConfigurationSection displaySettingsConf = conf.getConfigurationSection("display");
            if (displaySettingsConf != null)
                displaySettings(new DisplaySettings.Builder(miniMessage).load(displaySettingsConf).build());

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

        public Builder allowProjectile(ProjectileSelection allowProjectile) {
            this.allowProjectile = Objects.requireNonNull(allowProjectile);
            return this;
        }

        public Builder enableProjectile(ProjectileSelection enableProjectile) {
            this.enableProjectile = enableProjectile;
            return this;
        }

        public Builder displaySettings(DisplaySettings displaySettings) {
            this.displaySettings = displaySettings;
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
