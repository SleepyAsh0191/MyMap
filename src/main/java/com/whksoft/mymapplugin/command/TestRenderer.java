package com.whksoft.mymapplugin.command;

import com.whksoft.mymapplugin.QRcode.QRCode;
import org.bukkit.entity.Player;
import org.bukkit.map.*;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class TestRenderer extends MapRenderer {

    // TODO: 2021/1/25 线程优化

    String str;
    Plugin plugin;

    public TestRenderer(String str,Plugin plugin) {
        this.str = str;
        this.plugin = plugin;
    }

    @Override
    public void render(final MapView map, final MapCanvas canvas, final Player player) {
        try {
            new BukkitRunnable() {
                public void run() {
                    if (player == null) this.cancel();
                    {//player.sendMessage("别骂了别骂了，在渲染了！");
                        map.setScale(MapView.Scale.FARTHEST);
                        canvas.drawImage(0, 0, new QRCode(str).getImg());
                    }
                }
            }.runTaskTimerAsynchronously(plugin, 1, 300);
        } catch (IllegalPluginAccessException ignored){
        }
    }
}
