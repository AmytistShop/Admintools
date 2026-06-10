package com.urvinigati.admintools.commands;

import com.urvinigati.admintools.util.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class HealCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandUtils.hasPermission(sender, "admintools.heal")) {
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(CommandUtils.PREFIX + "§eИспользование: §f/heal <ник>");
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            sender.sendMessage(CommandUtils.PREFIX + "§cИгрок не найден или не онлайн: §e" + args[0]);
            return true;
        }

        CommandUtils.heal(target);
        sender.sendMessage(CommandUtils.PREFIX + "§aИгрок §e" + target.getName() + " §aполностью вылечен.");
        if (!sender.equals(target)) {
            target.sendMessage(CommandUtils.PREFIX + "§aТебя полностью вылечили.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) {
            return List.of();
        }
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            names.add(player.getName());
        }
        return CommandUtils.filterStartsWith(names, args[0]);
    }
}
