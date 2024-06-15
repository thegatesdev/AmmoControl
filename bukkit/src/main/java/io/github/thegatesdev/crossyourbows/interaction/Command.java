package io.github.thegatesdev.crossyourbows.interaction;

import io.github.thegatesdev.crossyourbows.*;
import io.github.thegatesdev.crossyourbows.data.*;
import io.github.thegatesdev.crossyourbows.handler.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.*;

import java.util.*;

import static io.github.thegatesdev.crossyourbows.interaction.Messages.*;

public final class Command implements CommandExecutor, TabCompleter {

    public static final String COMMAND_NAME = "crossyourbows";
    public static final List<String> subcommands = List.of("reload", "apply", "reset");

    private final CrossYourBowsBukkit plugin;
    private List<String> configurationNames;

    public Command(CrossYourBowsBukkit plugin, Settings settings) {
        this.plugin = plugin;
        applySettings(settings);
    }

    public void applySettings(Settings settings) {
        this.configurationNames = Arrays.asList(settings.configNames().toArray(new String[0]));
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (args.length) {
            default -> Collections.emptyList();
            case 1 -> subcommands;
            case 2 -> args[0].equalsIgnoreCase("apply") ? configurationNames : Collections.emptyList();
        };
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int currentArg = 0;
        if (args.length == 0) return true; // TODO show help

        String arg = args[currentArg].toLowerCase();
        switch (arg) {
            case "reload" -> handleReload(sender);
            case "apply" -> handleApply(sender, currentArg + 1, args);
            case "reset" -> handleReset(sender);
            default -> warn(sender, "Unknown subcommand '%s'!".formatted(arg));
        }

        return true;
    }


    private void handleReload(CommandSender sender) {
        plugin.reload();
        success(sender, "Plugin was reloaded!");
    }

    private void handleApply(CommandSender sender, int currentArg, String[] args) {
        if (!(sender instanceof Player player)) return; // TODO show help
        if (args.length <= currentArg) return; // TODO show help
        String configName = args[currentArg];

        ItemStack toApply = player.getInventory().getItemInMainHand();
        if (toApply.getType() != Material.CROSSBOW) {
            toApply = player.getInventory().getItemInOffHand();
            if (toApply.getType() != Material.CROSSBOW) return; // TODO show help
        }

        CrossbowMeta meta = (CrossbowMeta) toApply.getItemMeta();
        GameplayHandler.ApplyResult result = plugin.gameplayHandler().applyFireConfig(meta, configName);
        switch (result) {
            case CONFIG_NOT_FOUND -> warn(sender, "Could not find '%s' configuration!".formatted(configName));
            case ITEM_IN_CROSSBOW -> warn(sender, "Cannot apply to charged crossbow!");
            case SUCCESS -> {
                toApply.setItemMeta(meta);
                success(player, "Applied '%s' configuration to crossbow in hand!".formatted(configName));
            }
        }
    }

    private void handleReset(CommandSender sender) {
        if (!(sender instanceof Player player)) return; // TODO show help

        ItemStack toReset = player.getInventory().getItemInMainHand();
        if (toReset.getType() != Material.CROSSBOW) {
            toReset = player.getInventory().getItemInOffHand();
            if (toReset.getType() != Material.CROSSBOW) return; // TODO show help
        }

        CrossbowMeta meta = (CrossbowMeta) toReset.getItemMeta();
        plugin.gameplayHandler().removeFireConfig(meta);

        toReset.setItemMeta(meta);

        success(player, "Removed configuration from crossbow in hand!");
    }
}
