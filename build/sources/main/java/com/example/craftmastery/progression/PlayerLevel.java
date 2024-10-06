package com.example.craftmastery.progression;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerLevel {
    private int level;
    private int experience;
    private int experienceToNextLevel;

    public PlayerLevel() {
        this.level = 1;
        this.experience = 0;
        this.experienceToNextLevel = calculateExperienceForNextLevel(1);
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getExperienceToNextLevel() {
        return experienceToNextLevel;
    }

    public void addExperience(int amount) {
        experience += amount;
        while (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    public void setLevel(int newLevel) {
        if (newLevel > level) {
            while (level < newLevel) {
                levelUp();
            }
        } else if (newLevel < level) {
            level = newLevel;
            experience = 0;
            experienceToNextLevel = calculateExperienceForNextLevel(level);
        }
    }

    private void levelUp() {
        experience -= experienceToNextLevel;
        level++;
        experienceToNextLevel = calculateExperienceForNextLevel(level);
    }

    private int calculateExperienceForNextLevel(int level) {
        // Пример формулы расчета опыта для следующего уровня
        // Можно настроить под свои нужды
        return 100 * level * level;
    }

    public float getProgressToNextLevel() {
        return (float) experience / experienceToNextLevel;
    }

    public void reset() {
        level = 1;
        experience = 0;
        experienceToNextLevel = calculateExperienceForNextLevel(1);
    }

    // Методы для сериализации/десериализации
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("Level", level);
        nbt.setInteger("Experience", experience);
        nbt.setInteger("ExperienceToNextLevel", experienceToNextLevel);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        level = nbt.getInteger("Level");
        experience = nbt.getInteger("Experience");
        experienceToNextLevel = nbt.getInteger("ExperienceToNextLevel");
    }

    @Override
    public String toString() {
        return "PlayerLevel{" +
                "level=" + level +
                ", experience=" + experience +
                ", experienceToNextLevel=" + experienceToNextLevel +
                '}';
    }
}
