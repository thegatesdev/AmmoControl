package io.github.thegatesdev.crossyourbows.handler;

import io.github.thegatesdev.crossyourbows.data.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.plugin.*;

import java.util.logging.*;

public final class DamageHandler implements Listener {

    private final Logger logger;
    private final Plugin executorPlugin;
    private Settings settings;


    public DamageHandler(Logger logger, Plugin executorPlugin, Settings settings) {
        this.logger = logger;
        this.executorPlugin = executorPlugin;
        applySettings(settings);
    }

    public void applySettings(Settings settings) {
        this.settings = settings;
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void handleArrowDamage(EntityDamageEvent inputEvent) {
        if (!(inputEvent instanceof EntityDamageByEntityEvent event)) return;
        // TODO rework with (currently unstable) DamageSource API
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
        EntityType entityType = event.getDamager().getType();

        boolean resetArrow = entityType == EntityType.ARROW && settings.noArrowDamageCooldown();
        boolean resetFirework = entityType == EntityType.FIREWORK_ROCKET && settings.noFireworkDamageCooldown();

        if ((resetArrow || resetFirework) && event.getEntity() instanceof LivingEntity hitEntity) {
            Bukkit.getScheduler().runTask(executorPlugin, () -> hitEntity.setNoDamageTicks(0));
        }
    }
}
