package com.example.craftmastery.event;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.progression.PlayerProgression;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID)
public class CraftingEventHandler {

    @SubscribeEvent
    public static void onItemCrafted(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack crafted = event.crafting;

        RecipeWrapper recipe = CraftingManager.getInstance().getRecipeForResult(crafted);
        if (recipe != null) {
            PlayerProgression progression = ProgressionManager.getInstance().getPlayerProgression(player);

            if (!progression.isRecipeUnlocked(recipe.getId())) {
                // Cancel the crafting event
                event.setCanceled(true);
                player.sendMessage(new TextComponentTranslation("message.craftmastery.recipe_locked"));
            } else {
                // Recipe is unlocked, allow crafting and potentially grant experience
                progression.addExperience(recipe.getExperienceReward());
                player.sendMessage(new TextComponentTranslation("message.craftmastery.recipe_crafted",
                        crafted.getDisplayName(), recipe.getExperienceReward()));
            }
        }
    }
}
