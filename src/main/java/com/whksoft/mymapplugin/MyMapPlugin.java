package com.whksoft.mymapplugin;

import com.whksoft.mymapplugin.Listener.lostItem;
import com.whksoft.mymapplugin.command.mapCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyMapPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info(Bukkit.getBukkitVersion());
        getLogger().info(Bukkit.getVersion());
        getLogger().info(Bukkit.getServer().getClass().getPackage().getName());
        this.getCommand("map").setExecutor(new mapCommand(this));
        Bukkit.getPluginManager().registerEvents(new lostItem(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
