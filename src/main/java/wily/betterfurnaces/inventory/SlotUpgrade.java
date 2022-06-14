package wily.betterfurnaces.inventory;

import wily.betterfurnaces.items.ItemUpgradeDamage;
import wily.betterfurnaces.items.ItemUpgrade;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import wily.betterfurnaces.tile.TileEntityForge;
import wily.betterfurnaces.tile.TileEntitySmeltingBase;

public class SlotUpgrade extends SlotItemHandler {
	TileEntitySmeltingBase te;
	public SlotUpgrade(TileEntitySmeltingBase te, int index, int x, int y) {
		super(te.getInventory(), index, x, y);
		this.te = te;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return isStackValid(stack) || (((ItemUpgrade) stack.getItem()).upgradeType != 1 && te instanceof TileEntityForge);
	}

	public boolean isStackValid(ItemStack stack) {
		return te.getInventory().isItemValid(getSlotIndex(), stack);
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

}
