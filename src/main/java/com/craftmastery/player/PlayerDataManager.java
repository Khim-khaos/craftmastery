package com.craftmastery.player;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private static PlayerDataManager instance;
    private Map<UUID, PlayerData> playerDataMap;

    private PlayerDataManager() {
        playerDataMap = new HashMap<>();
    }

    public static PlayerDataManager getInstance() {
        if (instance == null) {
            instance = new PlayerDataManager();
        }
        return instance;
    }

    public PlayerData getPlayerData(EntityPlayer player) {
        UUID playerId = player.getUniqueID();
        return playerDataMap.computeIfAbsent(playerId, k -> new PlayerData());
    }

    public void savePlayerData(EntityPlayer player) {
        UUID playerId = player.getUniqueID();
        PlayerData data = playerDataMap.get(playerId);
        if (data != null) {
            // Сохранение данных игрока
        }
    }

    public void loadPlayerData(EntityPlayer player) {
        UUID playerId = player.getUniqueID();
        PlayerData data = new PlayerData();
        // Загрузка данных игрока
        playerDataMap.put(playerId, data);
    }
}
