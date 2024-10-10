package com.craftmastery.specialization;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class Specialization {
    private String id;
    private String name;
    private String description;
    private int requiredLevel;
    private Set<String> recipes;

    public Specialization() {
        this.recipes = new HashSet<>();
    }

    public Specialization(String id, String name, String description) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.requiredLevel = 1; // Default required level
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public void addRecipe(String recipeId) {
        recipes.add(recipeId);
    }

    public void removeRecipe(String recipeId) {
        recipes.remove(recipeId);
    }

    public boolean hasRecipe(String recipeId) {
        return recipes.contains(recipeId);
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setString("Id", id);
        compound.setString("Name", name);
        compound.setString("Description", description);
        compound.setInteger("RequiredLevel", requiredLevel);

        NBTTagList recipeList = new NBTTagList();
        for (String recipe : recipes) {
            recipeList.appendTag(new NBTTagString(recipe));
        }
        compound.setTag("Recipes", recipeList);
    }

    public void readFromNBT(NBTTagCompound compound) {
        id = compound.getString("Id");
        name = compound.getString("Name");
        description = compound.getString("Description");
        requiredLevel = compound.getInteger("RequiredLevel");

        recipes.clear();
        NBTTagList recipeList = compound.getTagList("Recipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < recipeList.tagCount(); i++) {
            recipes.add(recipeList.getStringTagAt(i));
        }
    }
}
