package com.urvinigati.admintools.commands;

import com.urvinigati.admintools.AdminToolsPlugin;
import com.urvinigati.admintools.listeners.AdminSwordListener;
import com.urvinigati.admintools.util.CommandUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class AdminSwordCommand implements CommandExecutor, TabCompleter {
    private final AdminToolsPlugin plugin;

    public AdminSwordCommand(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!CommandUtils.hasPermission(sender, "admintools.admin_sword")) {
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(CommandUtils.PREFIX + "§cЭту команду может использовать только игрок.");
            return true;
        }

        ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = sword.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§c§lAdmin Sword");
            meta.setLore(List.of(
                    "§7Админский меч с огромным уроном.",
                    "§7Убивает игроков и мобов при ударе."
            ));
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.getPersistentDataContainer().set(AdminSwordListener.ADMIN_SWORD_KEY(plugin), org.bukkit.persistence.PersistentDataType.BYTE, (byte) 1);
            sword.setItemMeta(meta);
        }

        player.getInventory().addItem(sword);
        player.sendMessage(CommandUtils.PREFIX + "§aТы получил §cAdmin Sword§a.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return List.of();
    }
}
