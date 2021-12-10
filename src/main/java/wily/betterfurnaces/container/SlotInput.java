package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;

public class SlotInput extends Slot {
    private BlockEntitySmeltingBase te;
    private BlockEntitySmeltingBase tf;
    public SlotInput(Container te, int index, int x, int y) {
        super(te, index, x, y);
        if (te instanceof BlockEntitySmeltingBase) {
            this.te = (BlockEntitySmeltingBase) te;
        }else if (te instanceof BlockEntitySmeltingBase) {
            this.tf = (BlockEntitySmeltingBase) te;
        }
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        if (te instanceof BlockEntitySmeltingBase) {
            return te.hasRecipe(stack);
        }else if (tf instanceof BlockEntitySmeltingBase) {
            return tf.hasRecipe(stack);
        }else return false;
    }
}
