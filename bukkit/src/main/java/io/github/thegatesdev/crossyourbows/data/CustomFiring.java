package io.github.thegatesdev.crossyourbows.data;

import java.util.*;

public final class CustomFiring {
    private final boolean arrowPickup;
    private final FirePattern pattern;
    private final double spreadX;
    private final double spreadY;

    private CustomFiring(boolean arrowPickup, FirePattern pattern, double spreadX, double spreadY) {
        this.arrowPickup = arrowPickup;
        this.pattern = pattern;
        this.spreadX = spreadX;
        this.spreadY = spreadY;
    }


    public boolean arrowPickup() {
        return arrowPickup;
    }

    public double spreadX() {
        return spreadX;
    }

    public double spreadY() {
        return spreadY;
    }

    public FirePattern getPattern() {
        return pattern;
    }


    public static class Builder {
        private boolean arrowPickup = true;
        private FirePattern pattern = null;
        private double spreadX = 0;
        private double spreadY = 0;


        public CustomFiring build() {
            Objects.requireNonNull(pattern, "pattern cannot be null");
            return new CustomFiring(arrowPickup, pattern, spreadX, spreadY);
        }


        public Builder arrowPickup(boolean arrowPickup) {
            this.arrowPickup = arrowPickup;
            return this;
        }

        public Builder pattern(FirePattern pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder spreadX(double spreadX) {
            this.spreadX = spreadX;
            return this;
        }

        public Builder spreadY(double spreadY) {
            this.spreadY = spreadY;
            return this;
        }
    }
}
