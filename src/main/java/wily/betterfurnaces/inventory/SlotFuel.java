package wily.betterfurnaces.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFuel extends SlotItemHandler {
	public SlotFuel(IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return isStackValid(stack);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return isEmptyBucket(stack) ? 1 : super.getItemStackLimit(stack);
	}

	public static boolean isStackValid(ItemStack stack) {
		return TileEntityFurnace.isItemFuel(stack) || isEmptyBucket(stack);
	}

	public static boolean isEmptyBucket(ItemStack stack) {
		return stack.getItem() == Items.BUCKET;
	}
}