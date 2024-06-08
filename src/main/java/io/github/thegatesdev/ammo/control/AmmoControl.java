package io.github.thegatesdev.ammo.control;

import io.github.thegatesdev.ammo.control.command.*;
import io.github.thegatesdev.ammo.control.config.*;
import io.github.thegatesdev.ammo.control.listener.*;
import org.bukkit.command.*;
import org.bukkit.plugin.java.*;

import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public final class AmmoControl extends JavaPlugin {

    private final Path configPath = getDataFolder().toPath().resolve("config.yml");

    private final Logger logger = getLogger();
    private final AmmoControlCommand command = new AmmoControlCommand(this);
    private AmmoListener listener;
    private AmmoConfig config;


    public void reload() {
        if (!config.load()) logger.warning("Failed to load configuration, using default values!");
        AmmoConfig.Settings settings = config.getActiveSettings().orElse(new AmmoConfig.Settings());
        listener.useSettings(settings);
        command.doTabCompletion(!settings.disableTabCompletion());
    }

    @Override
    public void onLoad() {
        config = new AmmoConfig(configPath);
        listener = new AmmoListener(this, new AmmoConfig.Settings(), logger);

        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(listener, this);
        PluginCommand pluginCommand = Objects.requireNonNull(getCommand("ammocontrol"));
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
        reload();
    }
}
