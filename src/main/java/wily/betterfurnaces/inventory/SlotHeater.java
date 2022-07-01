package wily.betterfurnaces.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.items.UpgradeItem;
import wily.betterfurnaces.tileentity.AbstractForgeTileEntity;

public class SlotHeater extends Slot {

    private final AbstractForgeTileEntity tf;

    public SlotHeater(IInventory te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        this.tf = (AbstractForgeTileEntity) te;
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
