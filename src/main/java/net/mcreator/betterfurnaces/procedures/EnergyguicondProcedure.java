package net.mcreator.betterfurnaces.procedures;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

import net.mcreator.betterfurnaces.BetterfurnacesreforgedModElements;
import net.mcreator.betterfurnaces.BetterfurnacesreforgedMod;

import java.util.Map;

@BetterfurnacesreforgedModElements.ModElement.Tag
public class EnergyguicondProcedure extends BetterfurnacesreforgedModElements.ModElement {
	public EnergyguicondProcedure(BetterfurnacesreforgedModElements instance) {
		super(instance, 79);
	}

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency x for procedure Energyguicond!");
			return false;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency y for procedure Energyguicond!");
			return false;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency z for procedure Energyguicond!");
			return false;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency world for procedure Energyguicond!");
			return false;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		IWorld world = (IWorld) dependencies.get("world");
		return ((new Object() {
			public boolean getValue(IWorld world, BlockPos pos, String tag) {
				TileEntity tileEntity = world.getTileEntity(pos);
				if (tileEntity != null)
					return tileEntity.getTileData().getBoolean(tag);
				return false;
			}
		}.getValue(world, new BlockPos((int) x, (int) y, (int) z), "energytank")) == (true));
	}
}
