package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.items.ItemUpgradeMisc;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockFurnaceTileBase;

public class SlotUpgrade extends Slot {

    private BlockFurnaceTileBase te;
    private BlockForgeTileBase tf;

    public SlotUpgrade(Container te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        if (te instanceof BlockFurnaceTileBase) {
            this.te = (BlockFurnaceTileBase) te;
        }else if (tf instanceof BlockForgeTileBase)
            this.tf = (BlockForgeTileBase) te;
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
        if (te instanceof BlockFurnaceTileBase) {
            te.onUpdateSent();
        }else if (tf instanceof BlockForgeTileBase)
            tf.onUpdateSent();
    }

}
