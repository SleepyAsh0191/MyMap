package com.whksoft.mymapplugin.command;

import com.whksoft.mymapplugin.MyMapPlugin;
import com.whksoft.mymapplugin.NMS.*;
import com.whksoft.mymapplugin.version.versionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class mapCommand implements CommandExecutor {

    Plugin plugin;

    public mapCommand(MyMapPlugin myMapPlugin) {
        plugin = myMapPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("map") && sender instanceof Player) {

            if (args.length != 1) {
                sender.sendMessage("输入语法错误");
                return true;
            }

            Player p = (Player) sender;

            //玩家背包里没有空位 这边暂时不考虑副手
            if (p.getInventory().firstEmpty() == -1) {
                sender.sendMessage("对不起你背包满了，清空一下背包吧");
                return true;
            }


            p.sendMessage("渲染中");

            //在玩家所在世界创建一个地图
            MapView view = Bukkit.createMap(p.getWorld());
            //清除当前地图的默认渲染
            for (MapRenderer mapRenderer : view.getRenderers()) {
                view.removeRenderer(mapRenderer);
            }
            //将此地图添加一个渲染器 注意：需要将TestRenderer继承自MapRenderer
            view.addRenderer(new TestRenderer(args[0], plugin));
            ItemStack renderedMap = new ItemStack(Material.FILLED_MAP, 1);
            // TODO: 2021/1/24 实现将NBT写入到ItemStack中
            //renderedMap.setDurability((short) view.getId());              /*此为旧版1.7.10方法*/
            //1.12 别忘了注释
            //ItemStack renderedMap = new ItemStack(Material.FILLED_MAP,1,(short) view.getId());

            //p.getInventory().addItem(renderedMap);

            //plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(),"minecraft:give "+p.getName()+" filled_map{\"map\":"+view.getId()+"}");

            //TODO: 老子去你妈的反射 不写了
            switch (versionUtils.getNMSVersion()) {
                case "1_14_R1":
                    renderedMap = new NBT1_14_R1(renderedMap).setMapID(view.getId());
                    break;
                case "1_15_R1":
                    renderedMap = new NBT1_15_R1(renderedMap).setMapID(view.getId());
                    break;
                case "1_16_R1":
                    renderedMap = new NBT1_16_R1(renderedMap).setMapID(view.getId());
                    break;
                case "1_16_R2":
                    renderedMap = new NBT1_16_R2(renderedMap).setMapID(view.getId());
                    break;
                case "1_16_R3":
                    renderedMap = new NBT1_16_R3(renderedMap).setMapID(view.getId());
                    break;
            }
            ItemMeta meta = renderedMap.getItemMeta();
            NamespacedKey key = new NamespacedKey(plugin, "isQRCode");
            assert meta != null;
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
            meta.setDisplayName("§a§l扫码地图");
            meta.setLore(Arrays.asList(
                    "§a用来测试的",
                    "§b这是第二行"));
            meta.addEnchant(Enchantment.KNOCKBACK, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            //这行代码有问题XD
            renderedMap.setItemMeta(meta);
            //给予玩家物品
            // TODO: 2021/1/26
            //  待办事项：
            //  1.优化给予物品的逻辑
            //  2.首先判断快捷栏是否为满 如果没满 看当前是否空手
            //      2.1如果空手 那么默认为选择空手
            //      2.2如果不是空手 看快捷栏有没有空余的位置 并且进行强制切换
            //  3.快捷栏满了的情况下 将当前选择的工具替换为地图 并且将已有工具放到背包中。
            //      3.1需要注意的是，支付完毕之后需要将物品进行还原！（可能暂时做不到）

            boolean hotBar = false;
            int emptyBar = 0;
            for (int i = 0; i < 9; i++) {
                ItemStack item = p.getInventory().getItem(i);
                if (item == null) {
                    hotBar = true;
                    emptyBar = i;
                }
            }


            if (hotBar) {
                if (p.getInventory().getItemInMainHand().getType() == Material.AIR) //主手为空
                {
                    p.getInventory().setItem(p.getInventory().getHeldItemSlot(), renderedMap);
                } else {
                    p.getInventory().setItem(emptyBar, renderedMap);
                    p.getInventory().setHeldItemSlot(emptyBar);
                }
            } else {
                // TODO: 2021/1/26 对快捷栏的设置
            }

            p.sendMessage("渲染完毕#" + view.getId());

            return true;
        }
        return false;
    }


}
