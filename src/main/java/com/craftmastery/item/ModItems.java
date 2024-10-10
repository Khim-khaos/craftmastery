package com.craftmastery.item;

import com.craftmastery.CraftMastery;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = CraftMastery.MODID)
public class ModItems {

    public static ItemRecipeBook RECIPE_BOOK_NORMAL;
    public static ItemRecipeBook RECIPE_BOOK_MAGIC;
    public static ItemRecipeBook RECIPE_BOOK_TECH;

    public static void init() {
        RECIPE_BOOK_NORMAL = new ItemRecipeBook("recipe_book_normal", "normal");
        RECIPE_BOOK_MAGIC = new ItemRecipeBook("recipe_book_magic", "magic");
        RECIPE_BOOK_TECH = new ItemRecipeBook("recipe_book_tech", "tech");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();

        registry.register(RECIPE_BOOK_NORMAL);
        registry.register(RECIPE_BOOK_MAGIC);
        registry.register(RECIPE_BOOK_TECH);
    }
}
