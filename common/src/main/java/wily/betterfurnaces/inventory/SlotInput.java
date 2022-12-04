package wily.betterfurnaces.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

public class SlotInput extends Slot {
    private final AbstractSmeltingBlockEntity be;
    public SlotInput(AbstractSmeltingBlockEntity be, int index, int x, int y) {
        super(be.inventory, index, x, y);
        this.be = be;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
            return be.hasRecipe(stack);
    }
}
