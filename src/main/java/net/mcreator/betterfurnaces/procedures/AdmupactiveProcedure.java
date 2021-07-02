package net.mcreator.betterfurnaces.procedures;

import net.minecraftforge.items.CapabilityItemHandler;

import net.minecraft.world.IWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;

import net.mcreator.betterfurnaces.item.AdmupgradeItem;
import net.mcreator.betterfurnaces.BetterfurnacesreforgedModElements;
import net.mcreator.betterfurnaces.BetterfurnacesreforgedMod;

import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

@BetterfurnacesreforgedModElements.ModElement.Tag
public class AdmupactiveProcedure extends BetterfurnacesreforgedModElements.ModElement {
	public AdmupactiveProcedure(BetterfurnacesreforgedModElements instance) {
		super(instance, 57);
	}

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("x") == null) {
			if (!dependencies.containsKey("x"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency x for procedure Admupactive!");
			return false;
		}
		if (dependencies.get("y") == null) {
			if (!dependencies.containsKey("y"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency y for procedure Admupactive!");
			return false;
		}
		if (dependencies.get("z") == null) {
			if (!dependencies.containsKey("z"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency z for procedure Admupactive!");
			return false;
		}
		if (dependencies.get("world") == null) {
			if (!dependencies.containsKey("world"))
				BetterfurnacesreforgedMod.LOGGER.warn("Failed to load dependency world for procedure Admupactive!");
			return false;
		}
		double x = dependencies.get("x") instanceof Integer ? (int) dependencies.get("x") : (double) dependencies.get("x");
		double y = dependencies.get("y") instanceof Integer ? (int) dependencies.get("y") : (double) dependencies.get("y");
		double z = dependencies.get("z") instanceof Integer ? (int) dependencies.get("z") : (double) dependencies.get("z");
		IWorld world = (IWorld) dependencies.get("world");
		double furnacetm = 0;
		return ((new ItemStack(AdmupgradeItem.block, (int) (1)).getItem() == (new Object() {
			public ItemStack getItemStack(BlockPos pos, int sltid) {
				AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
				TileEntity _ent = world.getTileEntity(pos);
				if (_ent != null) {
					_ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
						_retval.set(capability.getStackInSlot(sltid).copy());
					});
				}
				return _retval.get();
			}
		}.getItemStack(new BlockPos((int) x, (int) y, (int) z), (int) (4))).getItem())
				|| ((new ItemStack(AdmupgradeItem.block, (int) (1)).getItem() == (new Object() {
					public ItemStack getItemStack(BlockPos pos, int sltid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						TileEntity _ent = world.getTileEntity(pos);
						if (_ent != null) {
							_ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
								_retval.set(capability.getStackInSlot(sltid).copy());
							});
						}
						return _retval.get();
					}
				}.getItemStack(new BlockPos((int) x, (int) y, (int) z), (int) (5))).getItem())
						|| (new ItemStack(AdmupgradeItem.block, (int) (1)).getItem() == (new Object() {
							public ItemStack getItemStack(BlockPos pos, int sltid) {
								AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
								TileEntity _ent = world.getTileEntity(pos);
								if (_ent != null) {
									_ent.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
										_retval.set(capability.getStackInSlot(sltid).copy());
									});
								}
								return _retval.get();
							}
						}.getItemStack(new BlockPos((int) x, (int) y, (int) z), (int) (6))).getItem())));
	}
}
