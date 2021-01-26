package com.whksoft.mymapplugin.version;

import org.bukkit.Bukkit;

public class versionUtils {
    public static String getNMSVersion(){
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].replace("v","");
    }
}
