package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.BlockEntityForgeBase;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemLiquidFuel;
import wily.betterfurnaces.items.ItemUpgrade;
import wily.betterfurnaces.items.ItemXpTank;

public class SlotUpgrade extends Slot {

    private BlockEntitySmeltingBase te;

    public SlotUpgrade(Container te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        if (te instanceof BlockEntitySmeltingBase) {
            this.te = (BlockEntitySmeltingBase) te;
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
        if (te instanceof BlockEntitySmeltingBase) {
            te.onUpdateSent();
        }
    }

}
