package com.urvinigati.admintools.commands;

import com.urvinigati.admintools.util.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;

public final class HealNearCommand implements CommandExecutor, TabCompleter {
    private static final List<String> TYPES = List.of("player", "mob", "full");
    private static final List<String> RADIUS_EXAMPLES = List.of("5", "10", "25", "50", "100");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandUtils.hasPermission(sender, "admintools.healnear")) {
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CommandUtils.PREFIX + "§cЭту команду может использовать только игрок.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(CommandUtils.PREFIX + "§eИспользование: §f/healnear <player|mob|full> <radius>");
            return true;
        }

        String type = args[0].toLowerCase();
        if (!TYPES.contains(type)) {
            sender.sendMessage(CommandUtils.PREFIX + "§cТип должен быть: §eplayer§c, §emob§c или §efull§c.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(CommandUtils.PREFIX + "§eУкажи радиус. Пример: §f/healnear " + type + " 25");
            return true;
        }

        Double radius = CommandUtils.parseRadius(sender, args[1]);
        if (radius == null) {
            return true;
        }

        int healed = 0;
        for (org.bukkit.entity.Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)) {
            if (!(entity instanceof LivingEntity living)) {
                continue;
            }

            boolean isPlayer = entity instanceof Player;
            boolean shouldHeal = switch (type) {
                case "player" -> isPlayer;
                case "mob" -> !isPlayer;
                case "full" -> true;
                default -> false;
            };

            if (shouldHeal) {
                CommandUtils.heal(living);
                healed++;
            }
        }

        sender.sendMessage(CommandUtils.PREFIX + "§aГотово. Вылечено существ: §e" + healed + "§a. Радиус: §e" + radius + "§a.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return CommandUtils.filterStartsWith(TYPES, args[0]);
        }
        if (args.length == 2) {
            return CommandUtils.filterStartsWith(RADIUS_EXAMPLES, args[1]);
        }
        return List.of();
    }
}
