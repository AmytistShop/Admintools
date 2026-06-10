package com.urvinigati.admintools.listeners;

import com.urvinigati.admintools.AdminToolsPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class AdminSwordListener implements Listener {
    private final AdminToolsPlugin plugin;

    public AdminSwordListener(AdminToolsPlugin plugin) {
        this.plugin = plugin;
    }

    public static NamespacedKey ADMIN_SWORD_KEY(AdminToolsPlugin plugin) {
        return new NamespacedKey(plugin, "admin_sword");
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity victim)) {
            return;
        }
        if (!isAdminSword(attacker.getInventory().getItemInMainHand())) {
            return;
        }

        event.setDamage(1_000_000.0D);
        victim.setHealth(0.0D);
    }

    private boolean isAdminSword(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(ADMIN_SWORD_KEY(plugin), PersistentDataType.BYTE);
    }
}
