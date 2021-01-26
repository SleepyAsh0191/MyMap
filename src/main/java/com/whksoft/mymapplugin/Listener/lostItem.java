package com.whksoft.mymapplugin.Listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;


public class lostItem implements Listener {

    Plugin plugin = Bukkit.getPluginManager().getPlugin("MyMapPlugin");

//     TODO: 2021/1/25
//      待办事项：
//      1.将背包是否为空改为检测快捷栏工具是否为空 （已被其他方案代替，详见mapCommand.java）
//      2.检测玩家切换工具 之后强制切换回去（可能不会实现）
//      3.检测玩家右键到物品展示框
//      4.检测玩家点击地图（有bug）
//      5.扫码完毕之后对此玩家取消注册事件，确保安全。
//      6.玩家右键地图直接取消支付
//      7.检测玩家死亡事件
//      8.检测玩家切换到副手

    @EventHandler(priority = EventPriority.HIGHEST)
    public void throwItems(PlayerDropItemEvent e){
        //FIXME:
        // 已有bug:创造模式下会导致数目翻倍
        NamespacedKey key = new NamespacedKey(plugin, "isQRCode");
        ItemStack itemStack = e.getItemDrop().getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        if(itemMeta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)){
            e.getPlayer().sendMessage("你丢你妈呢？");
            e.setCancelled(true);
            itemStack.setAmount(1);
        }
    }

    //玩家右键快捷栏工具事件
    @EventHandler(priority = EventPriority.HIGHEST)
    public void clickItems(PlayerInteractEvent e){
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction()==Action.LEFT_CLICK_BLOCK) return;
        EquipmentSlot equipmentSlot = e.getHand();
        assert equipmentSlot != null;
        if (equipmentSlot.equals(EquipmentSlot.HAND))       //修复了一个智障的问题 触发两次语句 真鸡巴烦
        {
            NamespacedKey key = new NamespacedKey(plugin, "isQRCode");
            //Inventory inventory = e.getClickedInventory();
            ItemStack clicked = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = null;
            if (clicked.getType() == Material.FILLED_MAP)
                itemMeta = clicked.getItemMeta();
            else {
                e.getPlayer().sendMessage("不是扫码地图:" + clicked.getType());
                return;
            }
            if(itemMeta==null) return;
            if (itemMeta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                e.getPlayer().sendMessage("检测到右键扫码地图，已经取消支付");
                e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
            }
        }
    }

    //监听玩家手持扫码地图 右键物品展示框
    @EventHandler(priority = EventPriority.HIGHEST)
    public void itemFrame(PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.ITEM_FRAME) {
            NamespacedKey key = new NamespacedKey(plugin, "isQRCode");
            ItemStack clicked = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = clicked.getItemMeta();
            if(itemMeta == null) return;
            if (itemMeta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                e.getPlayer().sendMessage("检测到右键物品展示框");
                e.getPlayer().getInventory().getItemInMainHand().setAmount(0);
            }
        }
    }

    //检测玩家开E移动物品
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void moveItem(InventoryClickEvent e){
//        // FIXME: 2021/1/25
//        //  已知bug(创造): 会刷两份出来 并且不能shift+x清除 除非右键
//        //if(e.getClickedInventory()==null) return;
//        if(Objects.requireNonNull(Objects.requireNonNull(e.getClickedInventory()).getItem(e.getSlot())).getType()==Material.FILLED_MAP){
//            ItemStack itemStack = e.getClickedInventory().getItem(e.getSlot());
//            NamespacedKey key = new NamespacedKey(plugin, "isQRCode");
//            if(itemStack == null) return;
//            ItemMeta itemMeta = itemStack.getItemMeta();
//            if(itemMeta == null) return;
//            if (itemMeta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
//                e.getWhoClicked().sendMessage("瞎几把点啥啊？");
//                e.setResult(Event.Result.DENY);
//            }
//        }
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeath(PlayerDeathEvent e){
        // TODO: 2021/1/25 检测玩家死亡
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerRespawn(PlayerRespawnEvent e){
        // TODO: 2021/1/25 检测玩家复活
    }
}
