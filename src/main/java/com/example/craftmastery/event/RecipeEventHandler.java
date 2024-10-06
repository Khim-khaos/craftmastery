package com.example.craftmastery.event;

import com.example.craftmastery.recipe.RecipeManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RecipeEventHandler {

    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
        for (IRecipe recipe : event.getRegistry()) {
            if (shouldLockRecipe(recipe)) {
                RecipeManager.lockVanillaRecipe(recipe);
            }
        }
    }

    private static boolean shouldLockRecipe(IRecipe recipe) {
        // Реализуйте логику определения, должен ли рецепт быть заблокирован
        return false;
    }
}
