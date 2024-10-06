package com.example.craftmastery.event;

import com.example.craftmastery.player.PlayerProgress;
import com.example.craftmastery.player.PlayerProgressManager;
import com.example.craftmastery.recipe.CustomRecipe;
import com.example.craftmastery.recipe.RecipeManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class CraftingEventHandler {

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack crafted = event.crafting;

        if (!crafted.isEmpty()) {
            CustomRecipe customRecipe = RecipeManager.getRecipeByOutput(crafted);

            if (customRecipe != null) {
                PlayerProgress progress = PlayerProgressManager.get(player).getPlayerProgress(player);

                if (progress.isRecipeUnlocked(customRecipe)) {
                    // Рецепт разблокирован, позволяем крафт и даем очки
                    progress.addCraftPoints(customRecipe.getPointReward());
                    PlayerProgressManager.get(player).markDirty();
                } else {
                    // Рецепт не разблокирован, отменяем крафт
                    event.setCanceled(true);
                }
            }
        }
    }
}
