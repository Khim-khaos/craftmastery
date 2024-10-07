package com.example.craftmastery.player;

import net.minecraft.nbt.NBTTagCompound;

public class PlayerData {
    private int points;
    private int resetPoints;
    private int undoPoints;

    public void addPoints(int amount) {
        this.points += amount;
    }

    public void addResetPoints(int amount) {
        this.resetPoints += amount;
    }

    public void addUndoPoints(int amount) {
        this.undoPoints += amount;
    }

    public void saveNBTData(NBTTagCompound compound) {
        compound.setInteger("points", points);
        compound.setInteger("resetPoints", resetPoints);
        compound.setInteger("undoPoints", undoPoints);
    }

    public void loadNBTData(NBTTagCompound compound) {
        points = compound.getInteger("points");
        resetPoints = compound.getInteger("resetPoints");
        undoPoints = compound.getInteger("undoPoints");
    }
}
