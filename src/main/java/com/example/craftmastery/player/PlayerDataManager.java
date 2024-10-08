package com.example.craftmastery.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public static PlayerData getPlayerData(EntityPlayer player) {
        return playerDataMap.get(player.getUniqueID());
    }

    public static void setPlayerData(EntityPlayer player, PlayerData data) {
        playerDataMap.put(player.getUniqueID(), data);
    }

    public static void removePlayerData(EntityPlayer player) {
        playerDataMap.remove(player.getUniqueID());
    }

    public static void savePlayerData(EntityPlayer player) {
        PlayerData data = getPlayerData(player);
        if (data != null) {
            NBTTagCompound nbt = player.getEntityData();
            NBTTagCompound persistedData = nbt.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            persistedData.setTag("CraftMasteryData", data.serializeNBT());
            nbt.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedData);
        }
    }

    public static void loadPlayerData(EntityPlayer player) {
        NBTTagCompound nbt = player.getEntityData();
        NBTTagCompound persistedData = nbt.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (persistedData.hasKey("CraftMasteryData", Constants.NBT.TAG_COMPOUND)) {
            PlayerData data = new PlayerData();
            data.deserializeNBT(persistedData.getCompoundTag("CraftMasteryData"));
            setPlayerData(player, data);
        } else {
            setPlayerData(player, new PlayerData());
        }
    }
}
