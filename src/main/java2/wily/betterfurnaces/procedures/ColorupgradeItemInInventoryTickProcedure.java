package wily.betterfurnaces.procedures;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;

import wily.betterfurnaces.BetterfurnacesreforgedMod;

import java.util.Map;

public class ColorupgradeItemInInventoryTickProcedure {
	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency entity for procedure ColorupgradeItemInInventoryTick!");
			return;
		}
		if (dependencies.get("itemstack") == null) {
			if (!dependencies.containsKey("itemstack"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency itemstack for procedure ColorupgradeItemInInventoryTick!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		ItemStack itemstack = (ItemStack) dependencies.get("itemstack");
		if ((((itemstack).getOrCreateTag().getDouble("color")) == 1)) {
			entity.getPersistentData().putString("colort", "Gray");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 2)) {
			entity.getPersistentData().putString("colort", "Red");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 3)) {
			entity.getPersistentData().putString("colort", "Pink");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 4)) {
			entity.getPersistentData().putString("colort", "Purple");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 5)) {
			entity.getPersistentData().putString("colort", "Gold");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 6)) {
			entity.getPersistentData().putString("colort", "Blue");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 7)) {
			entity.getPersistentData().putString("colort", "Ice");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 8)) {
			entity.getPersistentData().putString("colort", "Lime");
		} else if ((((itemstack).getOrCreateTag().getDouble("color")) == 9)) {
			entity.getPersistentData().putString("colort", "Green");
		}
	}
}
