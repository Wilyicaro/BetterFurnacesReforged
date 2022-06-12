package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;

public class SlotInput extends Slot {
    private BlockEntitySmeltingBase te;
    public SlotInput(Container te, int index, int x, int y) {
        super(te, index, x, y);
        this.te = (BlockEntitySmeltingBase) te;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
            return te.hasRecipe(stack);
    }
}
