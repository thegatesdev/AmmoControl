package io.github.thegatesdev.crossyourbows.handler;

import io.github.thegatesdev.crossyourbows.data.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

import java.util.logging.*;

public final class DamageHandler implements Listener {

    private final Logger logger;
    private Settings settings;


    public DamageHandler(Logger logger, Settings settings) {
        this.logger = logger;
        applySettings(settings);
    }

    public void applySettings(Settings settings) {
        this.settings = settings;
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void handleArrowDamage(EntityDamageEvent inputEvent) {
        if (!(inputEvent instanceof EntityDamageByEntityEvent event)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
        logger.info("Damage entity type =" + event.getDamager().getType().name());
        // TODO check if 'damager' is arrow or player, then disable damage cooldown
        // TODO add damage cooldown setting to settings
    }
}
