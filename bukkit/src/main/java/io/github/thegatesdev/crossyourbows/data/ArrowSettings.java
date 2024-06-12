package io.github.thegatesdev.crossyourbows.data;

import org.bukkit.configuration.*;

public final class ArrowSettings {

    private final int knockBack;
    private final double damage;
    private final int pierce;
    private final boolean critical;

    private ArrowSettings(int knockBack, double damage, int pierce, boolean critical) {
        this.knockBack = knockBack;
        this.damage = damage;
        this.pierce = pierce;
        this.critical = critical;
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
        private int knockBack = 1;
        private double damage = 9.0;
        private int pierce = 1;
        private boolean critical = true;

        public Builder() {
        }

        public Builder(Builder other) {
            this.knockBack = other.knockBack;
            this.damage = other.damage;
            this.pierce = other.pierce;
            this.critical = other.critical;
        }


        public ArrowSettings build() {
            return new ArrowSettings(knockBack, damage, pierce, critical);
        }

        public Builder load(ConfigurationSection conf) {
            knockBack(conf.getInt("knockback", knockBack));
            damage(conf.getDouble("damage", damage));
            pierce(conf.getInt("pierce", pierce));
            critical(conf.getBoolean("critical", critical));
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
