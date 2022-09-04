package wily.betterfurnaces.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.items.UpgradeItem;

public class SlotHeater extends Slot {

    private AbstractSmeltingBlockEntity tf;

    public SlotHeater(Container be, int slotIndex, int xPosition, int yPosition) {
        super(be, slotIndex, xPosition, yPosition);
            this.tf = (AbstractSmeltingBlockEntity) be;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem && ((UpgradeItem) stack.getItem()).upgradeType == 1;
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
            tf.onUpdateSent();
    }

}
