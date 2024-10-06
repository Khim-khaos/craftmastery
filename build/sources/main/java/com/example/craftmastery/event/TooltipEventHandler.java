package com.example.craftmastery.event;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.config.ModConfig;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import com.example.craftmastery.progression.ProgressionManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID)
public class TooltipEventHandler {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (!ModConfig.general.enableItemTooltips) {
            return;
        }

        EntityPlayer player = event.getEntityPlayer();
        if (player == null) {
            return;
        }

        RecipeWrapper recipe = CraftingManager.getInstance().getRecipeForResult(event.getItemStack());
        if (recipe != null) {
            boolean isUnlocked = ProgressionManager.getInstance().getPlayerProgression(player).isRecipeUnlocked(recipe.getId());

            if (isUnlocked) {
                event.getToolTip().add(I18n.format("tooltip.craftmastery.recipe_unlocked"));
            } else {
                event.getToolTip().add(I18n.format("tooltip.craftmastery.recipe_locked"));
                event.getToolTip().add(I18n.format("tooltip.craftmastery.required_level", recipe.getRequiredLevel()));
                event.getToolTip().add(I18n.format("tooltip.craftmastery.point_cost", recipe.getPointCost()));
            }
        }
    }
}
