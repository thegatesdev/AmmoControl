package io.github.thegatesdev.crossyourbows.handler;

import io.github.thegatesdev.crossyourbows.data.*;
import io.papermc.paper.event.entity.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.*;

import java.util.*;
import java.util.logging.*;

public final class GameplayHandler implements Listener {

    private static final String PERM_USAGE = "crossyourbows.enable";
    private static final NamespacedKey KEY_CHARGE_COUNT = new NamespacedKey("crossyourbows", "charge_count");
    private static final NamespacedKey KEY_FIRE_CONFIG_NAME = new NamespacedKey("crossyourbows", "fire_config_name");

    private final Logger logger;
    private Settings settings;

    public GameplayHandler(Logger logger, Settings settings) {
        this.logger = logger;
        applySettings(settings);
    }


    public void applySettings(Settings settings) {
        this.settings = settings;
    }

    private Optional<FireConfiguration> configForItem(ItemMeta itemMeta) {
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        String nameValue = pdc.get(KEY_FIRE_CONFIG_NAME, PersistentDataType.STRING);
        if (nameValue == null) return settings.defaultConfig();
        return settings.namedConfig(nameValue);
    }

    private boolean chargedIsFirework(PlayerInventory inventory, EquipmentSlot usedHand) {
        ItemStack offhandItem = inventory.getItem(usedHand.getOppositeHand());
        @SuppressWarnings("ConstantValue") // This @NotNull guarantee may change in the future for paper.
        boolean firework = offhandItem != null && offhandItem.getType() == Material.FIREWORK_ROCKET;
        return firework; // TODO is there some better way to check which projectile was consumed?
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
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
        Optional<FireConfiguration> opFireConfig = configForItem(bowMeta);
        if (opFireConfig.isEmpty()) return;
        FireConfiguration fireConfig = opFireConfig.get();

        // Determine which projectile was charged, and if it should be ignored.
        ProjectileSelection selection = fireConfig.projectileSelection();
        if (selection != ProjectileSelection.BOTH) {
            boolean firework = chargedIsFirework(player.getInventory(), event.getHand());
            if (firework != (selection == ProjectileSelection.FIREWORK)) return;
        }

        // Should the item be consumed?
        event.setConsumeItem(fireConfig.consumeItem());

        // Add charge data if needed
        int maxCharges = fireConfig.maxCharges();
        if (maxCharges > 1) {
            PersistentDataContainer pdc = bowMeta.getPersistentDataContainer();
            pdc.set(KEY_CHARGE_COUNT, PersistentDataType.INTEGER, maxCharges);
            metaChanged = true;
        }

        if (metaChanged) bowItem.setItemMeta(bowMeta);
    }
}
