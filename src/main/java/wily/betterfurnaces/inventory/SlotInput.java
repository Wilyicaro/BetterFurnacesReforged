package wily.betterfurnaces.inventory;

import wily.betterfurnaces.utils.OreProcessingRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotInput extends SlotItemHandler {

	public SlotInput(IItemHandler inventoryIn, int slotIndex, int xPosition, int yPosition) {
		super(inventoryIn, slotIndex, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return isStackValid(stack);
	}

	public static boolean isStackValid(ItemStack stack) {
		return !OreProcessingRegistry.getSmeltingResult(stack).isEmpty() || !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
	}
}