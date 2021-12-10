package wily.betterfurnaces.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

public class SlotFuel extends Slot {

    public SlotFuel(IInventory te, int index, int x, int y) {
        super(te, index, x, y);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return BlockSmeltingTileBase.isItemFuel(stack) || isBucket(stack);
    }

    public int getMaxStackSize(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}
