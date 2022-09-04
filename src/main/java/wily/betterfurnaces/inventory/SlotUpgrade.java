package wily.betterfurnaces.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

public class SlotUpgrade extends Slot {

    private AbstractSmeltingBlockEntity be;

    public SlotUpgrade(Container be, int slotIndex, int xPosition, int yPosition) {
        super(be, slotIndex, xPosition, yPosition);
        if (be instanceof AbstractSmeltingBlockEntity) {
            this.be = (AbstractSmeltingBlockEntity) be;
        }
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return be.IisItemValidForSlot(getSlotIndex(), stack);
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        if (be instanceof AbstractSmeltingBlockEntity) {
            be.onUpdateSent();
        }
    }

}
