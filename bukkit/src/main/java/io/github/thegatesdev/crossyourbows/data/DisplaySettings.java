package io.github.thegatesdev.crossyourbows.data;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.minimessage.*;
import org.bukkit.configuration.*;

import java.util.*;

public final class DisplaySettings {

    private final Component name;
    private final List<Component> lore;

    private DisplaySettings(Builder builder) {
        this.name = builder.name;
        this.lore = builder.lore;
    }


    public Optional<Component> name() {
        return Optional.ofNullable(name);
    }

    public Optional<List<Component>> lore() {
        return Optional.ofNullable(lore);
    }


    public static final class Builder {

        private final MiniMessage miniMessage;
        private Component name = null;
        private List<Component> lore = null;


        public Builder(MiniMessage miniMessage) {
            this.miniMessage = miniMessage;
        }

        public Builder(MiniMessage miniMessage, Builder other) {
            this.miniMessage = miniMessage;
            this.name = other.name;
            this.lore = other.lore;
        }


        public DisplaySettings build() {
            return new DisplaySettings(this);
        }

        public Builder load(ConfigurationSection conf) {
            String displayName = conf.getString("name");
            if (displayName != null) name(miniMessage.deserialize(displayName));
            List<String> loreLines = conf.getStringList("lore");
            if (!loreLines.isEmpty()) lore(loreLines.stream().map(miniMessage::deserialize).toList());

            return this;
        }


        public Builder name(Component name) {
            this.name = name;
            return this;
        }

        public Builder lore(List<Component> lore) {
            this.lore = lore;
            return this;
        }
    }
}
