package com.example.craftmastery.progression;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.network.NetworkHandler;
import com.example.craftmastery.network.messages.SyncProgressionMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProgressionManager {
    private static ProgressionManager instance;

    private final Map<UUID, PlayerProgression> playerProgressions;

    private ProgressionManager() {
        playerProgressions = new HashMap<>();
    }

    public static ProgressionManager getInstance() {
        if (instance == null) {
            instance = new ProgressionManager();
        }
        return instance;
    }

    public PlayerProgression getPlayerProgression(EntityPlayer player) {
        if (player instanceof FakePlayer) {
            return new PlayerProgression(); // Возвращаем новый экземпляр для FakePlayer
        }

        return playerProgressions.computeIfAbsent(player.getUniqueID(), k -> new PlayerProgression());
    }

    public void setPlayerProgression(EntityPlayer player, PlayerProgression progression) {
        if (!(player instanceof FakePlayer)) {
            playerProgressions.put(player.getUniqueID(), progression);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            syncProgressionToClient((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        playerProgressions.remove(event.player.getUniqueID());
    }

    public void syncProgressionToClient(EntityPlayerMP player) {
        PlayerProgression progression = getPlayerProgression(player);
        NBTTagCompound nbt = new NBTTagCompound();
        progression.writeToNBT(nbt);
        NetworkHandler.INSTANCE.sendTo(new SyncProgressionMessage(nbt), player);
    }

    public void handleSyncFromServer(EntityPlayer player, NBTTagCompound nbt) {
        PlayerProgression progression = getPlayerProgression(player);
        progression.readFromNBT(nbt);
    }

    public void saveProgressionData(EntityPlayer player, NBTTagCompound compound) {
        PlayerProgression progression = getPlayerProgression(player);
        NBTTagCompound progressionNBT = new NBTTagCompound();
        progression.writeToNBT(progressionNBT);
        compound.setTag(CraftMastery.MODID + "_progression", progressionNBT);
    }

    public void loadProgressionData(EntityPlayer player, NBTTagCompound compound) {
        if (compound.hasKey(CraftMastery.MODID + "_progression")) {
            NBTTagCompound progressionNBT = compound.getCompoundTag(CraftMastery.MODID + "_progression");
            PlayerProgression progression = new PlayerProgression();
            progression.readFromNBT(progressionNBT);
            setPlayerProgression(player, progression);
        }
    }

    // Дополнительные методы для управления прогрессией

    public void addExperience(EntityPlayer player, int amount) {
        PlayerProgression progression = getPlayerProgression(player);
        progression.addExperience(amount);
        if (player instanceof EntityPlayerMP) {
            syncProgressionToClient((EntityPlayerMP) player);
        }
    }

    public void unlockRecipe(EntityPlayer player, String recipeId) {
        PlayerProgression progression = getPlayerProgression(player);
        // Здесь должна быть логика для получения RecipeWrapper по recipeId
        // RecipeWrapper recipe = CraftingManager.getInstance().getRecipe(recipeId);
        // if (recipe != null && progression.unlockRecipe(recipe)) {
        //     if (player instanceof EntityPlayerMP) {
        //         syncProgressionToClient((EntityPlayerMP) player);
        //     }
        // }
    }

    public void unlockUpgrade(EntityPlayer player, String upgradeId, int cost) {
        PlayerProgression progression = getPlayerProgression(player);
        if (progression.unlockUpgrade(upgradeId, cost)) {
            if (player instanceof EntityPlayerMP) {
                syncProgressionToClient((EntityPlayerMP) player);
            }
        }
    }

    public void resetProgress(EntityPlayer player) {
        PlayerProgression progression = getPlayerProgression(player);
        progression.resetProgress();
        if (player instanceof EntityPlayerMP) {
            syncProgressionToClient((EntityPlayerMP) player);
        }
    }
}
