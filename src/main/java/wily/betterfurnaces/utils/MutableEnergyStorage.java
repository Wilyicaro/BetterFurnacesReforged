package wily.betterfurnaces.utils;

import net.minecraftforge.energy.EnergyStorage;

/**
 * Extension of energy storage to allow for energy to be set.
 * @author Shadows
 *
 */
public class MutableEnergyStorage extends EnergyStorage {

	public MutableEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

}
