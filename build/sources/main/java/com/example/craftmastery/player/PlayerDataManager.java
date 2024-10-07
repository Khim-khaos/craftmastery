package com.example.craftmastery.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public static PlayerData getPlayerData(EntityPlayer player) {
        return playerDataMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerData());
    }

    public static void savePlayerData(EntityPlayer player) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound data = new NBTTagCompound();
        getPlayerData(player).saveNBTData(data);
        playerData.setTag("CraftMastery", data);
    }

    public static void loadPlayerData(EntityPlayer player) {
        NBTTagCompound playerData = player.getEntityData();
        NBTTagCompound data = playerData.getCompoundTag("CraftMastery");
        getPlayerData(player).loadNBTData(data);
    }
}
