package com.whksoft.mymapplugin.NMS;

import org.bukkit.inventory.ItemStack;

public abstract class NBT {

    ItemStack item;

    public NBT(ItemStack item){
        this.item = item;
    }

    public abstract ItemStack setMapID(int value);
}
