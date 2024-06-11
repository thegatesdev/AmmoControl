package io.github.thegatesdev.ammo.control;

import org.bukkit.plugin.java.*;

import java.util.logging.*;

public final class AmmoControlBukkit extends JavaPlugin {

    private final Logger logger = getLogger();

    @Override
    public void onEnable() {
        logger.info("Plugin stub");
        getServer().getPluginManager().disablePlugin(this);
    }
}
