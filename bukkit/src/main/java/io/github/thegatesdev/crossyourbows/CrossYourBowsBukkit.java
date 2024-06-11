package io.github.thegatesdev.crossyourbows;

import org.bukkit.plugin.java.*;

import java.util.logging.*;

public final class CrossYourBowsBukkit extends JavaPlugin {

    private final Logger logger = getLogger();

    @Override
    public void onEnable() {
        logger.info("Plugin stub");
        getServer().getPluginManager().disablePlugin(this);
    }
}
