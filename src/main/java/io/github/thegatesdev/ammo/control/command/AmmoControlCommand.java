package io.github.thegatesdev.ammo.control.command;

import io.github.thegatesdev.ammo.control.*;
import org.bukkit.command.*;
import org.jetbrains.annotations.*;

import java.util.*;

public final class AmmoControlCommand implements CommandExecutor, TabCompleter {

    private static final List<String> subCommandList = List.of("reload");
    private final AmmoControl ammoControl;
    private boolean doTabCompletion = true;

    public AmmoControlCommand(AmmoControl ammoControl) {
        this.ammoControl = ammoControl;
    }

    public void doTabCompletion(boolean doTabCompletion) {
        this.doTabCompletion = doTabCompletion;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;
        if (!args[0].equals("reload")) return false;
        ammoControl.reload();
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!doTabCompletion) return Collections.emptyList();
        if (args.length < 1) return subCommandList;
        if (args.length == 1) {
            if ("reload".startsWith(args[0])) return subCommandList;
        }
        return Collections.emptyList();
    }
}
