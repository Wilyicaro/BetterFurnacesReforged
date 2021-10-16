package wily.betterfurnaces.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockFurnaceTileBase;

public class SlotInput extends Slot {
    private BlockFurnaceTileBase te;
    private BlockForgeTileBase tf;
    public SlotInput(IInventory te, int index, int x, int y) {
        super(te, index, x, y);
        if (te instanceof BlockFurnaceTileBase) {
            this.te = (BlockFurnaceTileBase) te;
        }else if (te instanceof BlockForgeTileBase) {
            this.tf = (BlockForgeTileBase) te;
        }
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        if (te instanceof BlockFurnaceTileBase) {
            return te.hasRecipe(stack);
        }else if (tf instanceof BlockForgeTileBase) {
            return tf.hasRecipe(stack);
        }else return false;
    }
}