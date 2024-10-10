package com.craftmastery.item;

import com.craftmastery.CraftMastery;
import com.craftmastery.gui.GuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemRecipeBook extends Item {
    private String recipeType;

    public ItemRecipeBook(String recipeType) {
        this.recipeType = recipeType;
        setUnlocalizedName("recipe_book_" + recipeType);
        setRegistryName("recipe_book_" + recipeType);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            playerIn.openGui(CraftMastery.instance, GuiHandler.GUI_RECIPE_BOOK, worldIn,
                    (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public String getRecipeType() {
        return recipeType;
    }
}
