package io.github.thegatesdev.crossyourbows.data;

public enum ProjectileSelection {
    ARROW, FIREWORK, BOTH;

    public static ProjectileSelection read(String value) {
        return switch (value) {
            case "arrow", "arrows" -> ARROW;
            case "firework", "fireworks" -> FIREWORK;
            case "both", "any" -> BOTH;
            default -> throw new IllegalStateException("Unexpected firework selection: " + value);
        };
    }
}
