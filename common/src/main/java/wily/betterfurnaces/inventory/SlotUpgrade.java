package wily.betterfurnaces.inventory;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;

public class SlotUpgrade extends Slot {

    private final InventoryBlockEntity be;

    public SlotUpgrade(InventoryBlockEntity be, int slotIndex, int xPosition, int yPosition) {
        super(be.inventory, slotIndex, xPosition, yPosition);
        this.be =  be;

    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return be.IisItemValidForSlot(index, stack);
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        if (be instanceof SmeltingBlockEntity smeltingBe) {
            smeltingBe.onUpdateSent();
        }
    }

}
