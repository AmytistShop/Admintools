package com.urvinigati.admintools;

import com.urvinigati.admintools.commands.AdminCommand;
import com.urvinigati.admintools.commands.AdminSwordCommand;
import com.urvinigati.admintools.commands.HealCommand;
import com.urvinigati.admintools.commands.HealNearCommand;
import com.urvinigati.admintools.listeners.AdminSwordListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class AdminToolsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        AdminSwordListener swordListener = new AdminSwordListener(this);
        getServer().getPluginManager().registerEvents(swordListener, this);

        registerCommand("admin_sword", new AdminSwordCommand(this));
        registerCommand("admin", new AdminCommand());
        registerCommand("heal", new HealCommand());
        registerCommand("healnear", new HealNearCommand());

        getLogger().info("AdminTools enabled.");
    }

    private void registerCommand(String name, Object executorAndCompleter) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            getLogger().warning("Command not found in plugin.yml: " + name);
            return;
        }
        if (executorAndCompleter instanceof org.bukkit.command.CommandExecutor executor) {
            command.setExecutor(executor);
        }
        if (executorAndCompleter instanceof org.bukkit.command.TabCompleter completer) {
            command.setTabCompleter(completer);
        }
    }
}
