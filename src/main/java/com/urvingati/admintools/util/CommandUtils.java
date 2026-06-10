package com.urvinigati.admintools.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CommandUtils {
    private CommandUtils() {}

    public static final String PREFIX = "§8[§cAdminTools§8] §f";

    public static boolean hasPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sender.sendMessage(PREFIX + "§cНет прав: §7" + permission);
        return false;
    }

    public static Double parseRadius(CommandSender sender, String text) {
        try {
            double radius = Double.parseDouble(text.replace(',', '.'));
            if (radius <= 0) {
                sender.sendMessage(PREFIX + "§cРадиус должен быть больше 0.");
                return null;
            }
            if (radius > 500) {
                sender.sendMessage(PREFIX + "§cСлишком большой радиус. Максимум: 500 блоков.");
                return null;
            }
            return radius;
        } catch (NumberFormatException exception) {
            sender.sendMessage(PREFIX + "§cРадиус должен быть числом. Пример: §e/admin kill mob 25");
            return null;
        }
    }

    public static List<String> filterStartsWith(Collection<String> values, String input) {
        String lowerInput = input.toLowerCase();
        List<String> result = new ArrayList<>();
        for (String value : values) {
            if (value.toLowerCase().startsWith(lowerInput)) {
                result.add(value);
            }
        }
        return result;
    }

    public static void heal(LivingEntity entity) {
        entity.setHealth(entity.getMaxHealth());
        entity.setFireTicks(0);
        entity.setRemainingAir(entity.getMaximumAir());
        if (entity instanceof Player player) {
            player.setFoodLevel(20);
            player.setSaturation(20.0F);
            player.setExhaustion(0.0F);
        }
    }
}
