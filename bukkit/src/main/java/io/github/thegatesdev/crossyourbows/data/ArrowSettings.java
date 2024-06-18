package io.github.thegatesdev.crossyourbows.data;

import org.bukkit.configuration.*;

public final class ArrowSettings {

    private final double speed;
    private final int knockBack;
    private final double damage;
    private final int pierce;
    private final boolean critical;

    // TODO Make constructor take the builder instead
    private ArrowSettings(Builder builder) {
        this.speed = builder.speed;
        this.knockBack = builder.knockBack;
        this.damage = builder.damage;
        this.pierce = builder.pierce;
        this.critical = builder.critical;
    }


    public double speed() {
        return speed;
    }

    public int knockBack() {
        return knockBack;
    }

    public double damage() {
        return damage;
    }

    public int pierce() {
        return pierce;
    }

    public boolean critical() {
        return critical;
    }


    public static class Builder {
        private double speed = 0;
        private int knockBack = 1;
        private double damage = 9.0;
        private int pierce = 1;
        private boolean critical = true;

        public Builder() {
        }

        public Builder(Builder other) {
            this.speed = other.speed;
            this.knockBack = other.knockBack;
            this.damage = other.damage;
            this.pierce = other.pierce;
            this.critical = other.critical;
        }


        public ArrowSettings build() {
            return new ArrowSettings(this);
        }

        public Builder load(ConfigurationSection conf) {
            speed(conf.getDouble("speed", speed));
            knockBack(conf.getInt("knockback", knockBack));
            damage(conf.getDouble("damage", damage));
            pierce(conf.getInt("pierce", pierce));
            critical(conf.getBoolean("critical", critical));
            return this;
        }


        public Builder speed(double speed) {
            this.speed = speed;
            return this;
        }

        public Builder knockBack(int knockBack) {
            this.knockBack = knockBack;
            return this;
        }

        public Builder damage(double damage) {
            this.damage = damage;
            return this;
        }

        public Builder pierce(int pierce) {
            this.pierce = pierce;
            return this;
        }

        public Builder critical(boolean critical) {
            this.critical = critical;
            return this;
        }
    }
}
