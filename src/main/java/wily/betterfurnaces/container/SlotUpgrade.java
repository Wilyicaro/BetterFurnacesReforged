package wily.betterfurnaces.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.items.ItemUpgrade;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

public class SlotUpgrade extends Slot {

    private BlockSmeltingTileBase te;

    public SlotUpgrade(IInventory te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        if (te instanceof BlockSmeltingTileBase) {
            this.te = (BlockSmeltingTileBase) te;
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
        if (te instanceof BlockSmeltingTileBase) te.onUpdateSent();
    }

}
