package io.github.thegatesdev.crossyourbows;

import io.github.thegatesdev.crossyourbows.data.*;
import io.github.thegatesdev.crossyourbows.handler.*;
import io.github.thegatesdev.crossyourbows.interaction.Command;
import org.bukkit.command.*;
import org.bukkit.plugin.java.*;

import java.util.*;
import java.util.logging.*;

public final class CrossYourBowsBukkit extends JavaPlugin {

    private final Logger logger = getLogger();
    private Settings currentSettings;
    private GameplayHandler gameplayHandler;
    private Command command;


    private void loadSettings() {
        reloadConfig();
        currentSettings = new Settings.Builder().load(getConfig()).build();
    }


    public void reload() {
        loadSettings();
        gameplayHandler.applySettings(currentSettings);
        command.applySettings(currentSettings);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        loadSettings();
        gameplayHandler = new GameplayHandler(logger, currentSettings);
        command = new Command(this, currentSettings);
    }

    @Override
    public void onEnable() {
        reload();
        getServer().getPluginManager().registerEvents(gameplayHandler, this);

        PluginCommand command = Objects.requireNonNull(getCommand(Command.COMMAND_NAME));
        command.setExecutor(this.command);
        command.setTabCompleter(this.command);
    }


    public GameplayHandler gameplayHandler() {
        return gameplayHandler;
    }
}
