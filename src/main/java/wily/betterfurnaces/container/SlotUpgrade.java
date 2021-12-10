package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;
import wily.betterfurnaces.items.ItemUpgradeMisc;

public class SlotUpgrade extends Slot {

    private BlockEntitySmeltingBase te;
    private BlockEntitySmeltingBase tf;

    public SlotUpgrade(Container te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        if (te instanceof BlockEntitySmeltingBase) {
            this.te = (BlockEntitySmeltingBase) te;
        }else if (tf instanceof BlockEntitySmeltingBase)
            this.tf = (BlockEntitySmeltingBase) te;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ItemUpgradeMisc;
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        if (te instanceof BlockEntitySmeltingBase) {
            te.onUpdateSent();
        }else if (tf instanceof BlockEntitySmeltingBase)
            tf.onUpdateSent();
    }

}
