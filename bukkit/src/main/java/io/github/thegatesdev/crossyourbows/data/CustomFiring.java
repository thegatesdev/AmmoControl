package io.github.thegatesdev.crossyourbows.data;

import java.util.*;

public final class CustomFiring {

    private final boolean arrowPickup;
    private final FirePattern pattern;
    private final double spreadX;
    private final double spreadY;

    private CustomFiring(Builder builder) {
        this.arrowPickup = builder.arrowPickup;
        this.pattern = builder.pattern;
        this.spreadX = builder.spreadX;
        this.spreadY = builder.spreadY;
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
            return new CustomFiring(this);
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
