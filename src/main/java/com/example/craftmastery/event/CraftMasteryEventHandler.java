package com.example.craftmastery.event;

import com.example.craftmastery.crafting.CraftRecipe;
import com.example.craftmastery.player.PlayerData;
import com.example.craftmastery.player.PlayerDataManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class CraftMasteryEventHandler {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack crafted = event.crafting;
        PlayerData playerData = PlayerDataManager.getPlayerData(player);

        if (!CraftRecipe.isItemAllowed(crafted.getItem()) && !playerData.isRecipeUnlocked(crafted)) {
            event.setCanceled(true);
            player.sendMessage(new TextComponentTranslation("message.craftmastery.cannot_craft"));
        }
    }

    @SubscribeEvent
    public static void onItemUse(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack item = event.getItemStack();
        PlayerData playerData = PlayerDataManager.getPlayerData(player);

        if (!CraftRecipe.isItemAllowed(item.getItem()) && !playerData.isRecipeUnlocked(item)) {
            event.setCanceled(true);
            player.sendMessage(new TextComponentTranslation("message.craftmastery.cannot_use"));
        }
    }
}
