package wily.betterfurnaces.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;

public class SlotInput extends Slot {
    private AbstractSmeltingTileEntity te;

    public SlotInput(IInventory te, int index, int x, int y) {
        super(te, index, x, y);
        if (te instanceof AbstractSmeltingTileEntity) {
            this.te = (AbstractSmeltingTileEntity) te;
        }
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        if (te != null) {
            return te.hasRecipe(stack);
        } else return false;
    }
}
