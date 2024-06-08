package io.github.thegatesdev.ammo.control.listener;

import io.github.thegatesdev.ammo.control.*;
import io.github.thegatesdev.ammo.control.config.*;
import io.papermc.paper.event.entity.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.permissions.*;
import org.bukkit.scheduler.*;

import java.util.*;
import java.util.logging.*;

public final class AmmoListener implements Listener {

    private final AmmoControl ammoControl;
    private final Logger logger;
    private AmmoConfig.Settings settings;
    private boolean handleShootTime = false;
    private boolean handleLoadTime = false;

    public AmmoListener(AmmoControl ammoControl, AmmoConfig.Settings settings, Logger logger) {
        this.ammoControl = ammoControl;
        this.logger = logger;
        useSettings(settings);
    }


    public void useSettings(AmmoConfig.Settings settings) {
        Objects.requireNonNull(settings, "settings is null");
        this.settings = settings;

        handleShootTime = settings.arrowMode() == AmmoConfig.ControlMode.KEEP_BOW
                || settings.fireworkMode() == AmmoConfig.ControlMode.KEEP_BOW;
        handleLoadTime = settings.arrowMode() == AmmoConfig.ControlMode.KEEP_ITEM
                || settings.fireworkMode() == AmmoConfig.ControlMode.KEEP_ITEM;
    }

    private void debug(String message) {
        if (settings.printDebug()) logger.info(message);
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!handleShootTime) return;
        if (!event.hasItem()) return;
        if (!event.getAction().isRightClick()) return;

        ItemStack bowItem = event.getItem();
        if (bowItem == null || bowItem.getType() != Material.CROSSBOW) return;
        Player player = event.getPlayer();

        debug("Handling PlayerInteractEvent event for " + player.getName());

        if (!(bowItem.getItemMeta() instanceof CrossbowMeta meta)) {
            logger.severe("Crossbow item did not have CrossbowMeta, report to plugin author!");
            return;
        }

        List<ItemStack> projectiles = meta.getChargedProjectiles();
        if (projectiles.isEmpty()) return;

        BukkitScheduler scheduler = Bukkit.getScheduler();

        if (settings.simpleProjectileCheck()) {
            if (!allowProjectile(projectiles.get(0)
                    .getType() == Material.FIREWORK_ROCKET, AmmoConfig.ControlMode.KEEP_BOW, player)) {
                return;
            }
            scheduler.runTask(ammoControl, () -> bowItem.setItemMeta(meta));
        } else {
            boolean allowFirework = allowProjectile(true, AmmoConfig.ControlMode.KEEP_BOW, player);
            boolean allowArrow = allowProjectile(false, AmmoConfig.ControlMode.KEEP_BOW, player);

            scheduler.runTaskAsynchronously(ammoControl, () -> {
                List<ItemStack> keptProjectiles = new ArrayList<>();
                for (ItemStack itemStack : projectiles) {
                    Material type = itemStack.getType();
                    if ((allowFirework && type == Material.FIREWORK_ROCKET) || (allowArrow && type != Material.FIREWORK_ROCKET)) {
                        keptProjectiles.add(itemStack);
                    }
                }
                scheduler.runTask(ammoControl, () -> {
                    meta.setChargedProjectiles(keptProjectiles);
                    bowItem.setItemMeta(meta);
                });
            });
        }
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onEntityLoadCrossbow(EntityLoadCrossbowEvent event) {
        if (!handleLoadTime) return;
        if (!(event.getEntity() instanceof Player player)) return;

        debug("Handling EntityLoadCrossbowEvent event for " + player.getName());

        // We don't know what projectile got charged... Bukkit!!!
        // Luckily, fireworks only charge when in the opposite hand... for now
        ItemStack oppositeItem = player.getInventory().getItem(getOppositeHandSlot(event.getHand()));
        @SuppressWarnings("ConstantValue") // Yeah, right... not null, until it suddenly becomes null
        boolean isFirework = oppositeItem != null && oppositeItem.getType() == Material.FIREWORK_ROCKET;

        if (allowProjectile(isFirework, AmmoConfig.ControlMode.KEEP_ITEM, player)) {
            event.setConsumeItem(false);
        }
    }

    private boolean allowProjectile(boolean fireWork, AmmoConfig.ControlMode mode, Permissible perm) {
        if (fireWork) {
            return settings.fireworkMode() == mode && perm.hasPermission("ammocontrol.enable.fireworks");
        } else {
            return settings.arrowMode() == mode && perm.hasPermission("ammocontrol.enable.arrows");
        }
    }

    private static EquipmentSlot getOppositeHandSlot(EquipmentSlot slot) {
        // Yeah, this method exists in 1.20.6 but thou always asked for a backport
        return switch (slot) {
            case HAND -> EquipmentSlot.OFF_HAND;
            case OFF_HAND -> EquipmentSlot.HAND;
            default ->
                    throw new IllegalArgumentException("Unable to determine an opposite hand for equipment slot: " + slot.name());
        };
    }
}
