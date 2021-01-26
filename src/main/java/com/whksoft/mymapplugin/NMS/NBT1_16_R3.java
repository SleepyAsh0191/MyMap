package com.whksoft.mymapplugin.NMS;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBT1_16_R3 extends NBT {


    public NBT1_16_R3(ItemStack item) {
        super(item);
    }

    public ItemStack setMapID(int value) {
        String key = "map";
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        // TODO: 新版本多了个 .a 挺阴间
        tag.set(key, NBTTagInt.a(value));
        nmsStack.setTag(tag);
        item = CraftItemStack.asBukkitCopy(nmsStack);
        return item;
    }
}
