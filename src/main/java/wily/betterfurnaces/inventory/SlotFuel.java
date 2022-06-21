package wily.betterfurnaces.inventory;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;

public class SlotFuel extends SlotItemHandler {
	TileEntitySmeltingBase te;
	public SlotFuel(TileEntitySmeltingBase te, int slotIndex, int xPosition, int yPosition) {
		super(te.getInventory(), slotIndex, xPosition, yPosition);
		this.te = te;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return isStackValid(te, stack);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return isContainer(stack) ? 1 : super.getItemStackLimit(stack);
	}

	public static boolean isStackValid(TileEntitySmeltingBase te, ItemStack stack) {
		return TileEntityFurnace.isItemFuel(stack) || isContainer(stack)  || stack.hasCapability(CapabilityEnergy.ENERGY, null) && te.isEnergy();
	}

	public static boolean isContainer(ItemStack stack) {
		Capability fluidHandler = CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
		return stack.hasCapability(fluidHandler, null) && FluidUtil.getFluidContained(stack) == null;
	}
}