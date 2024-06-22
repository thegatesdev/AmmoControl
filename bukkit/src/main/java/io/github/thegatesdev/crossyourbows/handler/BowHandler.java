package io.github.thegatesdev.crossyourbows.handler;

import io.github.thegatesdev.crossyourbows.data.*;
import io.papermc.paper.event.entity.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.*;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.*;
import java.util.logging.*;

import static io.github.thegatesdev.crossyourbows.interaction.Messages.*;

public final class BowHandler implements Listener {

    private static final String PERM_USAGE = "crossyourbows.usage";
    private static final NamespacedKey KEY_CHARGE_COUNT = new NamespacedKey("crossyourbows", "charge_count");
    private static final NamespacedKey KEY_FIRE_CONFIG_NAME = new NamespacedKey("crossyourbows", "fire_config_name");

    private final Logger logger;
    private Settings settings;

    private volatile AfterFireCallback afterFireCallback;

    public BowHandler(Logger logger, Settings settings) {
        this.logger = logger;
        applySettings(settings);
    }

    // TODO permission per item


    public void applySettings(Settings settings) {
        this.settings = settings;
    }


    public ApplyResult applyFireConfig(CrossbowMeta crossbowMeta, String name) {
        Optional<BowConfiguration> opBowConfig = settings.namedConfig(name);
        if (opBowConfig.isEmpty()) return ApplyResult.CONFIG_NOT_FOUND;
        BowConfiguration bowConfig = opBowConfig.get();
        if (crossbowMeta.hasChargedProjectiles()) return ApplyResult.ITEM_IN_CROSSBOW;

        // Apply persistent data
        var pdc = crossbowMeta.getPersistentDataContainer();
        pdc.set(KEY_FIRE_CONFIG_NAME, PersistentDataType.STRING, name);
        pdc.remove(KEY_CHARGE_COUNT);

        // Apply custom bow settings
        bowConfig.displaySettings().ifPresent(displaySettings -> {
            displaySettings.lore().ifPresent(crossbowMeta::lore);
            displaySettings.name().ifPresent(crossbowMeta::itemName);
        });

        return ApplyResult.SUCCESS;
    }

    public void removeFireConfig(CrossbowMeta crossbowMeta) {
        // Remove persistent data
        var pdc = crossbowMeta.getPersistentDataContainer();
        pdc.remove(KEY_FIRE_CONFIG_NAME);
        pdc.remove(KEY_CHARGE_COUNT);

        // Reset custom bow settings
        crossbowMeta.lore(null);
        if (crossbowMeta.hasItemName()) crossbowMeta.itemName(null);
    }


    private Optional<BowConfiguration> configForItem(PersistentDataContainer pdc) {
        String nameValue = pdc.get(KEY_FIRE_CONFIG_NAME, PersistentDataType.STRING);
        if (nameValue == null) return settings.defaultConfig();
        return settings.namedConfig(nameValue);
    }

    private boolean chargedIsFirework(PlayerInventory inventory, EquipmentSlot usedHand) {
        // This method relies on the opposite hand being the only way to load firework rockets.
        ItemStack offhandItem = inventory.getItem(usedHand.getOppositeHand());
        @SuppressWarnings("ConstantValue") // This @NotNull guarantee may change in the future for paper.
        boolean firework = offhandItem != null && offhandItem.getType() == Material.FIREWORK_ROCKET;
        return firework; // TODO is there some better way to check which projectile was consumed?
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void handleCrossbowCharge(EntityLoadCrossbowEvent event) {
        // Check for player (other entities can charge crossbows).
        if (!(event.getEntity() instanceof Player player)) return;
        // If permissions are enabled, the player needs a permission for the bow settings to apply.
        if (settings.requirePermission() && !player.hasPermission(PERM_USAGE)) return;

        ItemStack bowItem = event.getCrossbow();
        CrossbowMeta bowMeta = (CrossbowMeta) bowItem.getItemMeta();
        boolean metaChanged = false;

        if (bowMeta == null) {
            // This should never happen, but notify anyway.
            logger.warning("Failed to get crossbow metadata!");
            return;
        }

        // Try getting the relevant configuration for this bow item (or the global defaults, if present).
        Optional<BowConfiguration> opFireConfig = configForItem(bowMeta.getPersistentDataContainer());
        if (opFireConfig.isEmpty()) return;
        BowConfiguration fireConfig = opFireConfig.get();
        boolean isFirework = chargedIsFirework(player.getInventory(), event.getHand());

        // Check if projectile is allowed to be charged
        ProjectileSelection allowSelection = fireConfig.allowProjectile();
        if (allowSelection != ProjectileSelection.BOTH) {
            if (isFirework != (allowSelection == ProjectileSelection.FIREWORK)) {
                event.setConsumeItem(false);
                event.setCancelled(true);
                warn(player, "This item cannot be loaded!");
                return;
            }
        }
        // Check if settings should be applied for projectile
        ProjectileSelection enableSelection = fireConfig.enableProjectile();
        if (enableSelection != ProjectileSelection.BOTH) {
            if (isFirework != (enableSelection == ProjectileSelection.FIREWORK)) return;
        }

        // Add charge data if needed
        int maxCharges = fireConfig.maxCharges();
        if (maxCharges > 1) {
            PersistentDataContainer pdc = bowMeta.getPersistentDataContainer();
            pdc.set(KEY_CHARGE_COUNT, PersistentDataType.INTEGER, maxCharges);
            metaChanged = true;
        }

        // Should the item be consumed?
        event.setConsumeItem(fireConfig.consumeItem());

        if (metaChanged) bowItem.setItemMeta(bowMeta);
    }

    @EventHandler(priority = EventPriority.HIGH)
    private void handleFireInteraction(PlayerInteractEvent event) {
        if (!event.hasItem()) return;
        if (!event.getAction().isRightClick()) return;

        // Get and check item data
        ItemStack bowItem = event.getItem();
        if (bowItem == null || bowItem.getType() != Material.CROSSBOW) return;
        ItemMeta bowMeta = bowItem.getItemMeta();
        if (bowMeta == null) return;
        PersistentDataContainer pdc = bowMeta.getPersistentDataContainer();

        // Find the configuration for this item
        Optional<BowConfiguration> opFireConfig = configForItem(pdc);
        if (opFireConfig.isEmpty()) return;
        BowConfiguration fireConfig = opFireConfig.get();

        // If permissions are enabled, the player needs a permission for the bow settings to apply.
        if (settings.requirePermission() && !event.getPlayer().hasPermission(PERM_USAGE)) {
            return;
        }

        // Check if the bow should keep its arrow (infinite charges, or some charges left).
        final boolean keepBowArrow;
        if (fireConfig.maxCharges() == 0) keepBowArrow = true; // Infinite charges
        else if (fireConfig.maxCharges() == 1) keepBowArrow = false; // Always one charge
        else {
            Integer charges = pdc.get(KEY_CHARGE_COUNT, PersistentDataType.INTEGER);
            if (charges != null && charges > 1) {
                // Charges never get to zero, if charge is equal to one the bow will unload when firing.
                pdc.set(KEY_CHARGE_COUNT, PersistentDataType.INTEGER, --charges);
                keepBowArrow = true;
            } else {
                keepBowArrow = false;
            }
        }

        int cooldown = fireConfig.fireCooldown();
        Optional<ArrowSettings> opArrowSettings = fireConfig.arrowSettings();
        afterFireCallback = new AfterFireCallback(event.getPlayer().getUniqueId(), (fireEvent, player) -> {
            ItemStack bowItemFire = fireEvent.getBow();
            if (bowItemFire == null || bowItemFire.getType() != Material.CROSSBOW) return;

            // Check if settings should be applied for projectile
            ProjectileSelection enableSelection = fireConfig.enableProjectile();
            if (enableSelection != ProjectileSelection.BOTH) {
                boolean arrow = (fireEvent.getProjectile() instanceof Arrow);
                if (arrow != (enableSelection == ProjectileSelection.ARROW)) return;
            }

            if (fireEvent.getProjectile() instanceof Arrow arrow) {
                opArrowSettings.ifPresent(st -> {
                    // TODO paper deprecated this in 1.21, wait for new API before this works again
                    // arrow.setKnockbackStrength(st.knockBack());
                    arrow.setPierceLevel(st.pierce());
                    arrow.setDamage(st.damage());
                    arrow.setCritical(st.critical());
                    if (st.speed() != 0) {
                        Vector dir = arrow.getVelocity().normalize();
                        arrow.setVelocity(dir.multiply(st.speed()));
                    }
                });
                // Set arrow to not be a pickup
                if (!fireConfig.pickupLastProjectile() || keepBowArrow) {
                    arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
                }
            }

            if (keepBowArrow) {
                // Reset to charged state
                bowItemFire.setItemMeta(bowMeta);
                // Optionally apply cooldown
                if (cooldown > 0) player.setCooldown(Material.CROSSBOW, cooldown);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    private void handleAfterFire(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        AfterFireCallback callback = afterFireCallback;
        if (callback == null) return;
        afterFireCallback = null;

        if (callback.playerID != player.getUniqueId()) {
            logger.warning("Plugin event order assumption failed! Crossbows may not work correctly, please report to the plugin author!");
            return;
        }
        callback.nextShootHandler.accept(event, player);
    }


    private record AfterFireCallback(UUID playerID,
            BiConsumer<EntityShootBowEvent, Player> nextShootHandler) {
    }

    public enum ApplyResult {
        SUCCESS, CONFIG_NOT_FOUND, ITEM_IN_CROSSBOW
    }
}
