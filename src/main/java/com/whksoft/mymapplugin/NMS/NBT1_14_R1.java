package com.whksoft.mymapplugin.NMS;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagInt;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBT1_14_R1 extends NBT{

    public NBT1_14_R1(ItemStack item) {
        super(item);
    }

    @Override
    public ItemStack setMapID(int value) {
        String key = "map";
        net.minecraft.server.v1_14_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = new NBTTagCompound();
        tag.set(key, new NBTTagInt(value));
        nmsStack.setTag(tag);
        item = CraftItemStack.asBukkitCopy(nmsStack);
        return item;
    }
}
