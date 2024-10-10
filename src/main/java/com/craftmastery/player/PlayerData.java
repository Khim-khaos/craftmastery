package com.craftmastery.player;

import com.craftmastery.config.ConfigHandler;
import com.craftmastery.specialization.Specialization;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Set;
import java.util.EnumMap;
import java.util.Map;

public class PlayerData {
    private int level;
    private int experience;
    private int learningPoints;
    private int craftResetPoints;
    private Set<String> unlockedRecipes;
    private Map<Specialization, Integer> specializations;

    public PlayerData() {
        this.level = 1;
        this.experience = 0;
        this.learningPoints = 0;
        this.craftResetPoints = 0;
        this.unlockedRecipes = new HashSet<>();
        this.specializations = new EnumMap<>(Specialization.class);
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getLearningPoints() {
        return learningPoints;
    }

    public void addExperience(int amount) {
        this.experience += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int expForNextLevel = getExpForNextLevel();
        while (this.experience >= expForNextLevel) {
            this.experience -= expForNextLevel;
            levelUp();
            expForNextLevel = getExpForNextLevel();
        }
    }

    private void levelUp() {
        this.level++;
        this.learningPoints += ConfigHandler.getBaseLearningPointsPerLevel();
        // Здесь можно добавить дополнительную логику при повышении уровня
    }

    public int getExpForNextLevel() {
        // Простая формула для расчета опыта, необходимого для следующего уровня
        return this.level * 100;
    }

    public boolean useLearningPoints(int amount) {
        if (this.learningPoints >= amount) {
            this.learningPoints -= amount;
            return true;
        }
        return false;
    }

    public void addLearningPoints(int amount) {
        this.learningPoints += amount;
    }

    public void unlockRecipe(String recipeId) {
        unlockedRecipes.add(recipeId);
    }

    public boolean hasUnlockedRecipe(String recipeId) {
        return unlockedRecipes.contains(recipeId);
    }

    public Set<String> getUnlockedRecipes() {
        return new HashSet<>(unlockedRecipes);
    }

    public boolean useCraftResetPoints(int amount) {
        if (this.craftResetPoints >= amount) {
            this.craftResetPoints -= amount;
            return true;
        }
        return false;
    }

    public void addCraftResetPoints(int amount) {
        this.craftResetPoints += amount;
    }

    public int getCraftResetPoints() {
        return craftResetPoints;
    }

    public void addSpecializationLevel(Specialization specialization, int levels) {
        specializations.put(specialization, specializations.getOrDefault(specialization, 0) + levels);
    }

    public int getSpecializationLevel(Specialization specialization) {
        return specializations.getOrDefault(specialization, 0);
    }

    public Map<Specialization, Integer> getSpecializations() {
        return new EnumMap<>(specializations);
    }

    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("Level", level);
        compound.setInteger("Experience", experience);
        compound.setInteger("LearningPoints", learningPoints);
        compound.setInteger("CraftResetPoints", craftResetPoints);

        NBTTagList recipeList = new NBTTagList();
        for (String recipe : unlockedRecipes) {
            recipeList.appendTag(new NBTTagString(recipe));
        }
        compound.setTag("UnlockedRecipes", recipeList);

        NBTTagCompound specializationsNBT = new NBTTagCompound();
        for (Map.Entry<Specialization, Integer> entry : specializations.entrySet()) {
            specializationsNBT.setInteger(entry.getKey().name(), entry.getValue());
        }
        compound.setTag("Specializations", specializationsNBT);
    }

    public void loadNBTData(NBTTagCompound compound) {
        level = compound.getInteger("Level");
        experience = compound.getInteger("Experience");
        learningPoints = compound.getInteger("LearningPoints");
        craftResetPoints = compound.getInteger("CraftResetPoints");

        unlockedRecipes.clear();
        NBTTagList recipeList = compound.getTagList("UnlockedRecipes", Constants.NBT.TAG_STRING);
        for (int i = 0; i < recipeList.tagCount(); i++) {
            unlockedRecipes.add(recipeList.getStringTagAt(i));
        }

        specializations.clear();
        NBTTagCompound specializationsNBT = compound.getCompoundTag("Specializations");
        for (Specialization spec : Specialization.values()) {
            if (specializationsNBT.hasKey(spec.name())) {
                specializations.put(spec, specializationsNBT.getInteger(spec.name()));
            }
        }
    }
}
