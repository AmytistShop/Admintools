package com.urvinigati.admintools.commands;

import com.urvinigati.admintools.util.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public final class AdminCommand implements CommandExecutor, TabCompleter {
    private static final List<String> SUB_COMMANDS = List.of("kill");
    private static final List<String> TYPES = List.of("player", "mob", "full");
    private static final List<String> RADIUS_EXAMPLES = List.of("5", "10", "25", "50", "100");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandUtils.hasPermission(sender, "admintools.admin")) {
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CommandUtils.PREFIX + "§cЭту команду может использовать только игрок.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(CommandUtils.PREFIX + "§eИспользование: §f/admin kill <player|mob|full> <radius>");
            return true;
        }

        if (!args[0].equalsIgnoreCase("kill")) {
            sender.sendMessage(CommandUtils.PREFIX + "§cНеизвестная команда. Используй: §e/admin kill <player|mob|full> <radius>");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(CommandUtils.PREFIX + "§eКого убить? §f/admin kill <player|mob|full> <radius>");
            return true;
        }

        String type = args[1].toLowerCase();
        if (!TYPES.contains(type)) {
            sender.sendMessage(CommandUtils.PREFIX + "§cТип должен быть: §eplayer§c, §emob§c или §efull§c.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(CommandUtils.PREFIX + "§eУкажи радиус. Пример: §f/admin kill " + type + " 25");
            return true;
        }

        Double radius = CommandUtils.parseRadius(sender, args[2]);
        if (radius == null) {
            return true;
        }

        int killed = 0;
        for (org.bukkit.entity.Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)) {
            if (!(entity instanceof LivingEntity living)) {
                continue;
            }
            if (entity.getUniqueId().equals(player.getUniqueId())) {
                continue;
            }

            boolean isPlayer = entity instanceof Player;
            boolean shouldKill = switch (type) {
                case "player" -> isPlayer;
                case "mob" -> !isPlayer;
                case "full" -> true;
                default -> false;
            };

            if (shouldKill) {
                living.setHealth(0.0D);
                killed++;
            }
        }

        sender.sendMessage(CommandUtils.PREFIX + "§aГотово. Убито существ: §e" + killed + "§a. Радиус: §e" + radius + "§a.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return CommandUtils.filterStartsWith(SUB_COMMANDS, args[0]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("kill")) {
            return CommandUtils.filterStartsWith(TYPES, args[1]);
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("kill")) {
            return CommandUtils.filterStartsWith(RADIUS_EXAMPLES, args[2]);
        }
        return List.of();
    }
}
