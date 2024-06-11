package io.github.thegatesdev.crossyourbows.data;

import org.bukkit.configuration.*;

import java.util.*;

public final class FireConfiguration {
    private final boolean consumeItem;
    private final int maxCharges;
    private final ProjectileSelection projectileSelection;
    private final CustomFiring firing;

    private FireConfiguration(boolean consumeItem, int maxCharges, ProjectileSelection projectileSelection, CustomFiring firing) {
        this.consumeItem = consumeItem;
        this.maxCharges = maxCharges;
        this.projectileSelection = projectileSelection;
        this.firing = firing;
    }


    public boolean consumeItem() {
        return consumeItem;
    }

    public int maxCharges() {
        return maxCharges;
    }

    public ProjectileSelection projectileSelection() {
        return projectileSelection;
    }

    public Optional<CustomFiring> firing() {
        return Optional.ofNullable(firing);
    }


    public static class Builder {
        private boolean consumeItem = true;
        private int maxCharges = 1;
        private ProjectileSelection projectileSelection = ProjectileSelection.BOTH;
        private CustomFiring firing = null;

        public Builder() {
        }

        public Builder(Builder other) {
            this.consumeItem = other.consumeItem;
            this.maxCharges = other.maxCharges;
            this.projectileSelection = other.projectileSelection;
            this.firing = other.firing;
        }


        public FireConfiguration build() {
            return new FireConfiguration(consumeItem, maxCharges, projectileSelection, firing);
        }

        public Builder load(ConfigurationSection conf) {
            consumeItem(conf.getBoolean("consume_item", consumeItem));
            maxCharges(conf.getInt("max_charges", maxCharges));
            String selectionString = conf.getString("allow_projectile");
            if (selectionString != null) projectileSelection(ProjectileSelection.read(selectionString));
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

        public Builder projectileSelection(ProjectileSelection projectileSelection) {
            this.projectileSelection = Objects.requireNonNull(projectileSelection);
            return this;
        }

        public Builder firing(CustomFiring firing) {
            this.firing = Objects.requireNonNull(firing);
            return this;
        }
    }
}
