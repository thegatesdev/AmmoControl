package io.github.thegatesdev.crossyourbows;

import io.github.thegatesdev.crossyourbows.data.*;
import io.github.thegatesdev.crossyourbows.handler.*;
import org.bukkit.plugin.java.*;

import java.util.logging.*;

public final class CrossYourBowsBukkit extends JavaPlugin {

    private final Logger logger = getLogger();
    private Settings currentSettings;
    private GameplayHandler gameplayHandler;


    private void reload() {
        reloadConfig();
        currentSettings = new Settings.Builder().load(getConfig()).build();
    }


    @Override
    public void onLoad() {
        saveDefaultConfig();
        reload();
        gameplayHandler = new GameplayHandler(logger, currentSettings);
    }

    @Override
    public void onEnable() {
        reload();
        gameplayHandler.applySettings(currentSettings);
        getServer().getPluginManager().registerEvents(gameplayHandler, this);
    }
}
