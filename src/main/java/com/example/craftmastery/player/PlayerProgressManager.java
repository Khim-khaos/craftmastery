package com.example.craftmastery.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerProgressManager extends WorldSavedData {
    private static final String DATA_NAME = "CraftMasteryPlayerProgress";
    private Map<UUID, PlayerProgress> playerProgressMap;

    public PlayerProgressManager() {
        super(DATA_NAME);
        this.playerProgressMap = new HashMap<>();
    }

    public PlayerProgress getPlayerProgress(EntityPlayer player) {
        return playerProgressMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerProgress());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        playerProgressMap.clear();
        for (String key : nbt.getKeySet()) {
            UUID playerUUID = UUID.fromString(key);
            NBTTagCompound playerData = nbt.getCompoundTag(key);
            PlayerProgress progress = new PlayerProgress();
            progress.readFromNBT(playerData);
            playerProgressMap.put(playerUUID, progress);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        for (Map.Entry<UUID, PlayerProgress> entry : playerProgressMap.entrySet()) {
            compound.setTag(entry.getKey().toString(), entry.getValue().writeToNBT());
        }
        return compound;
    }

    public static PlayerProgressManager get(EntityPlayer player) {
        PlayerProgressManager instance = (PlayerProgressManager) player.world.getPerWorldStorage().getOrLoadData(PlayerProgressManager.class, DATA_NAME);
        if (instance == null) {
            instance = new PlayerProgressManager();
            player.world.getPerWorldStorage().setData(DATA_NAME, instance);
        }
        return instance;
    }
}
