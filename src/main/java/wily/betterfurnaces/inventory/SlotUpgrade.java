package wily.betterfurnaces.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;

public class SlotUpgrade extends Slot {

    private AbstractSmeltingTileEntity te;

    public SlotUpgrade(IInventory te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        if (te instanceof AbstractSmeltingTileEntity) {
            this.te = (AbstractSmeltingTileEntity) te;
        }
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return te.IisItemValidForSlot(getSlotIndex(), stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        if (te != null) te.onUpdateSent();
    }

}
