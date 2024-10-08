package com.example.craftmastery.crafting;

import com.example.craftmastery.player.PlayerData;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CraftRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    private ResourceLocation id;
    private ItemStack output;
    private NonNullList<Ingredient> ingredients;
    private int pointCost;
    private List<ResourceLocation> dependencies;
    private String category;
    private boolean isShapeless;

    public CraftRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> ingredients, int pointCost, String category, boolean isShapeless) {
        this.id = id;
        this.output = output;
        this.ingredients = ingredients;
        this.pointCost = pointCost;
        this.dependencies = new ArrayList<>();
        this.category = category;
        this.isShapeless = isShapeless;
        setRegistryName(id);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        if (isShapeless) {
            return matchesShapeless(inv);
        } else {
            return matchesShaped(inv);
        }
    }

    public boolean matches(List<ItemStack> inputs) {
        if (isShapeless) {
            return matchesShapelessList(inputs);
        } else {
            return matchesShapedList(inputs);
        }
    }

    private boolean matchesShapeless(InventoryCrafting inv) {
        List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stackInSlot = inv.getStackInSlot(i);
            if (!stackInSlot.isEmpty()) {
                boolean matched = false;
                for (Ingredient ingredient : remainingIngredients) {
                    if (ingredient.apply(stackInSlot)) {
                        matched = true;
                        remainingIngredients.remove(ingredient);
                        break;
                    }
                }
                if (!matched) {
                    return false;
                }
            }
        }
        return remainingIngredients.isEmpty();
    }

    private boolean matchesShaped(InventoryCrafting inv) {
        for (int i = 0; i <= 3 - getRecipeWidth(); ++i) {
            for (int j = 0; j <= 3 - getRecipeHeight(); ++j) {
                if (checkMatch(inv, i, j, true)) {
                    return true;
                }
                if (checkMatch(inv, i, j, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchesShapelessList(List<ItemStack> inputs) {
        List<Ingredient> remainingIngredients = new ArrayList<>(ingredients);
        for (ItemStack input : inputs) {
            if (!input.isEmpty()) {
                boolean matched = false;
                for (Ingredient ingredient : remainingIngredients) {
                    if (ingredient.apply(input)) {
                        matched = true;
                        remainingIngredients.remove(ingredient);
                        break;
                    }
                }
                if (!matched) {
                    return false;
                }
            }
        }
        return remainingIngredients.isEmpty();
    }

    private boolean matchesShapedList(List<ItemStack> inputs) {
        if (inputs.size() != ingredients.size()) {
            return false;
        }
        for (int i = 0; i < inputs.size(); i++) {
            if (!ingredients.get(i).apply(inputs.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
        for (int x = 0; x < 3; ++x) {
            for (int y = 0; y < 3; ++y) {
                int recipeX = x - startX;
                int recipeY = y - startY;
                Ingredient target = Ingredient.EMPTY;
                if (recipeX >= 0 && recipeY >= 0 && recipeX < getRecipeWidth() && recipeY < getRecipeHeight()) {
                    if (mirror) {
                        target = ingredients.get(getRecipeWidth() - recipeX - 1 + recipeY * getRecipeWidth());
                    } else {
                        target = ingredients.get(recipeX + recipeY * getRecipeWidth());
                    }
                }
                if (!target.apply(inv.getStackInSlot(x + y * inv.getWidth()))) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getRecipeWidth() {
        return 3; // Предполагаем, что рецепт 3x3. Измените, если нужно поддерживать другие размеры.
    }

    private int getRecipeHeight() {
        return 3; // Предполагаем, что рецепт 3x3. Измените, если нужно поддерживать другие размеры.
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= getRecipeWidth() && height >= getRecipeHeight();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ResourceLocation getId() {
        return id;
    }

    public int getPointCost() {
        return pointCost;
    }

    public String getCategory() {
        return category;
    }

    public void addDependency(ResourceLocation recipeId) {
        dependencies.add(recipeId);
    }

    public List<ResourceLocation> getDependencies() {
        return new ArrayList<>(dependencies);
    }

    public boolean canBeUnlocked(PlayerData playerData) {
        for (ResourceLocation dependency : dependencies) {
            if (!playerData.isRecipeUnlocked(dependency)) {
                return false;
            }
        }
        return true;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public String getType() {
        return isShapeless ? "shapeless" : "shaped";
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < remainingItems.size(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            remainingItems.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }
        return remainingItems;
    }

    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override
    public String getGroup() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CraftRecipe that = (CraftRecipe) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CraftRecipe{" +
                "id=" + id +
                ", output=" + output +
                ", ingredients=" + ingredients +
                ", pointCost=" + pointCost +
                ", dependencies=" + dependencies +
                ", category='" + category + '\'' +
                ", isShapeless=" + isShapeless +
                '}';
    }
}
