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
    private GameplayHandler gameplayHandler;
    private Command command;


    private Settings loadSettings() {
        reloadConfig();
        return new Settings.Builder().load(getConfig()).build();
    }


    public void reload() {
        Settings settings = loadSettings();

        gameplayHandler.applySettings(settings);
        command.applySettings(settings);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        Settings settings = loadSettings();

        gameplayHandler = new GameplayHandler(logger, settings);
        command = new Command(this, settings);
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
