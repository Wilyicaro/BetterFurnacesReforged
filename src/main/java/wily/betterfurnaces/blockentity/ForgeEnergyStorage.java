package wily.betterfurnaces.blockentity;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag> {
    private static final String KEY = "energy";
    private int energy;
    private int capacity;
    private int maxInOut = 1000000;
    private AbstractSmeltingBlockEntity tile;

    public ForgeEnergyStorage(AbstractSmeltingBlockEntity tile, int energy, int capacity) {
        this.energy = energy;
        this.capacity = capacity;
        this.tile = tile;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt(KEY, this.energy);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.energy = nbt.getInt(KEY);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = Math.min(capacity - energy, Math.min(this.maxInOut, maxReceive));

        if (!simulate) {
            energy += energyReceived;
            this.tile.setChanged();
        }

        return energyReceived;
    }

    public int consumeEnergy(int maxExtract, boolean simulate) {
        int energyExtracted = Math.min(energy, Math.min(this.maxInOut, maxExtract));

        if (!simulate)
            energy -= energyExtracted;

        return energyExtracted;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return this.energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public String toString() {
        return "ForgeEmergyStorage{" +
                "energy=" + energy +
                ", capacity=" + capacity +
                ", maxInOut=" + maxInOut +
                '}';
    }
}