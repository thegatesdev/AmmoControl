package io.github.thegatesdev.crossyourbows;

import io.github.thegatesdev.crossyourbows.data.*;
import org.bukkit.plugin.java.*;

import java.util.logging.*;

public final class CrossYourBowsBukkit extends JavaPlugin {

    private final Logger logger = getLogger();
    private Settings currentSettings;


    private void reload() {
        reloadConfig();
        currentSettings = new Settings.Builder().load(getConfig()).build();
    }


    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        reload();
    }
}
