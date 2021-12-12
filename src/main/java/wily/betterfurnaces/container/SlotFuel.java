package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;

public class SlotFuel extends Slot {

    public SlotFuel(Container te, int index, int x, int y) {
        super(te, index, x, y);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return BlockEntitySmeltingBase.isItemFuel(stack) || isBucket(stack);
    }

    public int getMaxStackSize(ItemStack stack) {
        return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isBucket(ItemStack stack) {
        return stack.getItem() == Items.BUCKET;
    }
}
