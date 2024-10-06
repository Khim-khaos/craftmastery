package com.example.craftmastery.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class LockedRecipe implements IRecipe {
    private final IRecipe baseRecipe;

    public LockedRecipe(IRecipe baseRecipe) {
        this.baseRecipe = baseRecipe;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return baseRecipe.canFit(width, height);
    }

    @Override
    public ItemStack getRecipeOutput() {
        return baseRecipe.getRecipeOutput();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public IRecipe setRegistryName(net.minecraft.util.ResourceLocation name) {
        return baseRecipe.setRegistryName(name);
    }

    @Override
    public net.minecraft.util.ResourceLocation getRegistryName() {
        return baseRecipe.getRegistryName();
    }

    @Override
    public Class<IRecipe> getRegistryType() {
        return baseRecipe.getRegistryType();
    }
}
