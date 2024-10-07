package com.example.craftmastery.player;

import com.example.craftmastery.CraftMastery;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerProgressProvider implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(PlayerProgress.class)
    public static final Capability<PlayerProgress> PLAYER_PROGRESS_CAPABILITY = null;

    private PlayerProgress instance = PLAYER_PROGRESS_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_PROGRESS_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_PROGRESS_CAPABILITY ? PLAYER_PROGRESS_CAPABILITY.cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) PLAYER_PROGRESS_CAPABILITY.getStorage().writeNBT(PLAYER_PROGRESS_CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        PLAYER_PROGRESS_CAPABILITY.getStorage().readNBT(PLAYER_PROGRESS_CAPABILITY, instance, null, nbt);
    }

    public static class Storage implements Capability.IStorage<PlayerProgress> {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<PlayerProgress> capability, PlayerProgress instance, EnumFacing side) {
            return instance.writeToNBT();
        }

        @Override
        public void readNBT(Capability<PlayerProgress> capability, PlayerProgress instance, EnumFacing side, NBTBase nbt) {
            if (nbt instanceof NBTTagCompound) {
                instance.readFromNBT((NBTTagCompound) nbt);
            }
        }
    }

    public static class Factory implements Callable<PlayerProgress> {
        @Override
        public PlayerProgress call() throws Exception {
            return new PlayerProgress();
        }
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(PlayerProgress.class, new Storage(), new Factory());
    }
}
