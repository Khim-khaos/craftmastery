package com.example.craftmastery.points;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointsManager extends WorldSavedData {
    private static final String DATA_NAME = "CraftMasteryPoints";
    private Map<UUID, PlayerPoints> playerPointsMap = new HashMap<>();

    public PointsManager() {
        super(DATA_NAME);
    }

    public PointsManager(String name) {
        super(name);
    }

    public static PointsManager get(World world) {
        PointsManager instance = (PointsManager) world.getMapStorage().getOrLoadData(PointsManager.class, DATA_NAME);
        if (instance == null) {
            instance = new PointsManager();
            world.getMapStorage().setData(DATA_NAME, instance);
        }
        return instance;
    }

    public PlayerPoints getPlayerPoints(EntityPlayer player) {
        return playerPointsMap.computeIfAbsent(player.getUniqueID(), k -> new PlayerPoints());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        playerPointsMap.clear();
        for (String key : nbt.getKeySet()) {
            UUID playerUUID = UUID.fromString(key);
            NBTTagCompound playerData = nbt.getCompoundTag(key);
            PlayerPoints points = new PlayerPoints();
            points.loadNBTData(playerData);
            playerPointsMap.put(playerUUID, points);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        for (Map.Entry<UUID, PlayerPoints> entry : playerPointsMap.entrySet()) {
            NBTTagCompound playerData = new NBTTagCompound();
            entry.getValue().saveNBTData(playerData);
            compound.setTag(entry.getKey().toString(), playerData);
        }
        return compound;
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
